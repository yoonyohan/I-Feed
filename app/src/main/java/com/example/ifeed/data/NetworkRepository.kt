package com.example.ifeed.data

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.example.ifeed.R
import com.example.ifeed.application.MyFeedApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NetworkRepository {
    suspend fun logIn(fieldsNotEmpty: Boolean, userName: String, password: String): LogInStages
    suspend fun accountCreation(email: String = "", password: String): AccountCreationStages
    suspend fun updateAccountDataLive(): AccountCreationStages
    suspend fun updateAccountDataNormal()
    suspend fun addProfileImage(uri: Uri): AddImage
    suspend fun updateProfileImageRealNormal()
    suspend fun getProfileImageUrl(): String
    suspend fun writeNewPost(): WritePost
    fun readAllPosts(completion: (List<FeedPost>) -> Unit)
}

class FireNetWorkRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val fireStorage: FirebaseStorage,
    private val fireRealTime: FirebaseDatabase,
    private val _appState: MutableStateFlow<AppState>,
    private val feedApplication: MyFeedApplication
) : NetworkRepository {
    override suspend fun logIn( fieldsNotEmpty: Boolean, userName: String, password: String): LogInStages {
        return withContext(Dispatchers.IO) {
            val deferred = CompletableDeferred<LogInStages>()
            if (fieldsNotEmpty) {
                deferred.complete(LogInStages.LogInError("Input Fields cannot be empty"))
            } else {
                withContext(Dispatchers.IO) {
                    firebaseAuth.signInWithEmailAndPassword( userName, password)
                        .addOnCompleteListener {
                                task ->
                            if (task.isSuccessful) {
                                deferred.complete(LogInStages.LogInSuccess)
                            }
                        }.addOnFailureListener { exception ->
                            // Handle specific login exceptions
                            val errorMessage = when (exception) {
                                is FirebaseAuthInvalidUserException -> "User not found"
                                is FirebaseAuthInvalidCredentialsException -> "Invalid password"
                                else -> "Authentication failed: ${exception.message ?: "Something went wrong"}"
                            }

                            deferred.complete(LogInStages.LogInError(errorMessage))
                        }
                }
            }

            deferred.await()
        }
    }
    override suspend fun accountCreation(email: String, password: String): AccountCreationStages {
        return withContext(Dispatchers.IO) {
            val deferred = CompletableDeferred<AccountCreationStages>()
            Log.d("Network Repository", "App state values check : $email and $password")
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        deferred.complete(AccountCreationStages.AccountCreationSuccess)
                    } else {
                        val errorMessage = task.exception?.localizedMessage ?: "Unknown error"
                        Log.e("NetworkRepository", errorMessage)
                        deferred.complete(AccountCreationStages.AccountCreationError(errorMessage))
                    }

                }.addOnFailureListener { exception ->
                    val errorMessage : String = when(exception) {
                        is FirebaseAuthWeakPasswordException -> "Weak password. Please choose a stronger one."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid Email"
                        is FirebaseAuthUserCollisionException -> "Account already exist for this email address"
                        else -> "Problem with creating an account"
                    }
                    deferred.complete(AccountCreationStages.AccountCreationError(errorMessage))
                }
            deferred.await()
        }
    }
    override suspend fun updateAccountDataLive(): AccountCreationStages {
        return withContext(Dispatchers.IO) {
            val deferred = CompletableDeferred<AccountCreationStages>()
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                val realTimeRef = fireRealTime.reference.child("account").child(userId)
                val accountModel = Account(
                    name = firestore.collection("userData").document(userId).get().await().getString("fullName") ?: "",
                    userId = userId,
                    profileImageUrl = "",
                    coverImageUrl = ""
                )
                realTimeRef.setValue(accountModel)
                    .addOnFailureListener {
                        deferred.complete(AccountCreationStages.AccountCreationError(feedApplication.getString(R.string.let_s_finish_account_later)))
                    }
            }
            deferred.await()
        }
    }
    override suspend fun updateAccountDataNormal() {
        withContext(Dispatchers.IO) {
            CompletableDeferred<AccountCreationStages>()
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val userData = hashMapOf(
                        "userId" to currentUser.uid,
                        "firstName" to _appState.value.firstName,
                        "lastName" to _appState.value.lastName,
                        "middleName" to _appState.value.middleName,
                        "profileImageUrl" to _appState.value.profileImageUrl,
                        "coverImageUrl" to _appState.value.coverImageUrl,
                        "fullName" to "${_appState.value.firstName} ${_appState.value.lastName}",
                        "userName" to _appState.value.userName,
                        "profileUrl" to _appState.value.profileUrl
                    )

                    firestore.collection("userData").document(currentUser.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                        }.addOnFailureListener {
                                e ->
                            Log.w(ContentValues.TAG, "Error writing document", e)
                        }
                } else {
                    Log.e("NetworkRepository", "User needs to log in for update normal data base")
                }
            } catch (e: Exception) {
                Log.e("NetworkRepository", "Problem with update userData to normal database", e)
            }
        }
    }
    override suspend fun addProfileImage(uri: Uri): AddImage {
        return withContext(Dispatchers.IO) {
            val deferred = CompletableDeferred<AddImage>()
            val currentUser = firebaseAuth.currentUser

            if (currentUser != null) {
                val userId = currentUser.uid

                val storageRef = fireStorage.reference.child("profile_images/$userId.jpg")
                storageRef.putFile(uri)
                    .addOnCompleteListener {task ->
                        if (task.isSuccessful) {
                            deferred.complete(AddImage.ImageAddSuccess)
                            _appState.update { // 1
                                it.copy(
                                    updateProfileImageWithDataBases = true,
                                    profileImageLoad = true
                                )
                            }
                        } else {
                            deferred.complete(AddImage.ImageAddError(feedApplication.getString(R.string.error_uploading_image)))
                        }
                    }.addOnFailureListener { // Needs to handle exceptions here
                        deferred.complete(AddImage.ImageAddError(feedApplication.getString(R.string.error_uploading_image)))
                    }
            }

            deferred.await()
        }
    }
    override suspend fun updateProfileImageRealNormal() {
        withContext(Dispatchers.IO) {
            try {
                val current = firebaseAuth.currentUser
                val storageRef = fireStorage.reference

                if (current != null) {
                    val userId = current.uid
                    val imageRef = storageRef.child("profile_images/$userId.jpg")

                    imageRef.downloadUrl.addOnSuccessListener { imageUri ->
                        val realTimeRef = fireRealTime.reference
                        val propertyName = "profileImageUrl"

                        val updateTag = hashMapOf<String, Any>(propertyName to imageUri.toString())

                        realTimeRef.child("account").child(userId).updateChildren(updateTag)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("NetworkRepository", "Profile image real time update completed")
                                } else {
                                    Log.e("NetworkRepository", "Profile image real time update failed")
                                }
                            }.addOnFailureListener { // Handle exceptions
                                Log.e("NetworkRepository", "Profile image real time update failed", it)
                            }

                        firestore.collection("userData").document(userId).update("profileImageUrl",imageUri.toString())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful)  {
                                    Log.d("NetworkRepository", "Profile image firestore update completed")
                                }
                            }
                            .addOnFailureListener {
                                Log.e("NetworkRepository", "Profile image normal update failed", it)
                            }

                    }.addOnFailureListener { // You should handle the exceptions
                        Log.e("NetworkRepository", "Problem with getting image uri", it)
                    }
                } else {
                    Log.e("NetworkRepository", "You should log first")
                }


            } catch (e: Exception) {
                Log.e("NetworkRepository", "Problem with getting image uri", e)
            }
        }
    }
    override suspend fun getProfileImageUrl(): String {
        return withContext(Dispatchers.IO) {
            val current = firebaseAuth.currentUser
            val differed = CompletableDeferred<String>()
            if (current != null) {
                val userId = current.uid
                val storageRef = fireStorage.reference
                val imageRef = storageRef.child("profile_images/$userId.jpg")

                imageRef.downloadUrl.addOnSuccessListener { imageUri ->
                    differed.complete(imageUri.toString())
                }.addOnFailureListener { // You should handle the exceptions
                    Log.e("NetworkRepository", "Problem with getting image uri", it)
                }
            } else {
                Log.e("NetworkRepository", "You should log first")
            }
            differed.await()
        }
    }
    override suspend fun writeNewPost(): WritePost {
        return withContext(Dispatchers.IO) {
            val deferred = CompletableDeferred<WritePost>()
            val currentUser = firebaseAuth.currentUser

            if (currentUser != null) {
                val postRef = fireRealTime.getReference("feed")
                val userId = currentUser.uid

                val postModel = FeedPost(
                    name = firestore.collection("userData").document(userId).get().await().getString("fullName") ?: "",
                    userId = userId,
                    content = _appState.value.content,
                    timestamp = System.currentTimeMillis()
                )

                val newPostRef = postRef.push()
                newPostRef.setValue(postModel)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            deferred.complete(WritePost.PostCreationSuccess)
                        }
                    }.addOnFailureListener { exception ->
                        val errorMessage = when (exception) {
                            is FirebaseFirestoreException -> "Firebase realtime exception: ${exception.message ?: "Something went wrong"}"
                            else -> "Failed to create post: ${exception.message ?: "Something went wrong"}"
                        }
                        deferred.complete(WritePost.PostCreationError(errorMessage))
                        Log.e("AddNewPost","Firestore exception", exception)
                    }
            }
            deferred.await()
        }
    }
    override fun readAllPosts(completion: (List<FeedPost>) -> Unit) {
        val postRef = fireRealTime.getReference("feed")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts = mutableListOf<FeedPost>()

                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(FeedPost::class.java)
                    post?.let {
                        posts.add(it)
                    }
                }

                completion(posts)
            }

            override fun onCancelled(error: DatabaseError) {
                completion(emptyList())
            }
        })
    }
}

data class Account(
    val name : String = "",
    val userId : String = "",
    val profileImageUrl : String = "",
    val coverImageUrl : String = ""
)

data class FeedPost(
    val name: String = "",
    val userId: String = "",
    val content: String = "",
    val timestamp: Long = 0
)


sealed class AccountCreationStages {
    data object AccountCreationSuccess: AccountCreationStages()
    data class AccountCreationError(val errorMessage: String): AccountCreationStages()
}

sealed class LogInStages {
    data object LogInSuccess: LogInStages()
    data class LogInError(val errorMsg: String): LogInStages()
}

sealed class AddImage {
    data object ImageAddSuccess: AddImage()
    data class ImageAddError(val errorMsg: String): AddImage()
}

sealed class WritePost {
    data object PostCreationSuccess: WritePost()
    data class PostCreationError(val errorMsg: String): WritePost()
}