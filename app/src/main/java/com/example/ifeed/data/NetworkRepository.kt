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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkRepository {
    suspend fun logIn()
    suspend fun accountCreation()
    suspend fun updateAccountDataLive()
    suspend fun updateAccountDataNormal()
    suspend fun addProfileImage(uri: Uri)
    suspend fun updateProfileImageRealNormal()
    suspend fun writeNewPost()
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
    override suspend fun logIn() {
        withContext(Dispatchers.IO) {
            if (_appState.value.userName.isEmpty() && _appState.value.password.isEmpty()) {
                _appState.update {
                    it.copy(
                        isEmpty = true,
                        alert = "Input Fields cannot be empty"
                    )
                }
            } else {
                try {
                    withContext(Dispatchers.IO) {
                        firebaseAuth.signInWithEmailAndPassword(
                            _appState.value.userName,
                            _appState.value.password
                        ).addOnCompleteListener {
                                task ->
                            if (task.isSuccessful) {
                                _appState.update {
                                    it.copy(
                                        alert = "Welcome back!",
                                        isLoggedIn = true,
                                    )
                                }
                            }
                        }.addOnFailureListener { exception ->
                            // Handle specific login exceptions
                            val errorMessage = when (exception) {
                                is FirebaseAuthInvalidUserException -> "User not found"
                                is FirebaseAuthInvalidCredentialsException -> "Invalid password"
                                else -> "Authentication failed: ${exception.message ?: "Something went wrong"}"
                            }

                            _appState.update {
                                it.copy(
                                    alert = errorMessage,
                                    isLoggedIn = false,
                                )
                            }
                        }
                    }

                } catch (e: Exception) {
                    // Handle other exceptions if needed
                    _appState.update {
                        it.copy(
                            alert = "Something went wrong",
                            isLoggedIn = false,
                        )
                    }
                }
            }
        }
    }
    override suspend fun accountCreation() {
        withContext(Dispatchers.IO) {
            try {
                if (_appState.value.emailAddressOn) { // If User Select email
                    Log.d("Network Repository", "App state values check : ${_appState.value.email} and ${_appState.value.password}")
                    firebaseAuth.createUserWithEmailAndPassword(_appState.value.email, _appState.value.password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _appState.update {
                                    it.copy(
                                        email = "", password = "", isLoading = true, toLoggedIn = true, accountCreationSuccess = false, toAccountCreation = true
                                    )
                                }
                            } else {
                                _appState.update {
                                    it.copy(
                                        alert = feedApplication.getString(R.string.problem_with_creating_an_account),
                                        isLoading = true
                                    )
                                }
                            }

                        }.addOnFailureListener { exception ->
                            val errorMessage : String = when(exception) {
                                is FirebaseAuthWeakPasswordException -> "Weak password. Please choose a stronger one."
                                is FirebaseAuthInvalidCredentialsException -> "Invalid Email"
                                is FirebaseAuthUserCollisionException -> "Account already exist for this email address"
                                else -> "Problem with creating an account"
                            }
                            _appState.update {
                                it.copy(alert = errorMessage, isLoading = true)
                            }
                        }
                }

                if (_appState.value.phoneNumberOn) { // If user select phone number
                    firebaseAuth.createUserWithEmailAndPassword(_appState.value.phoneNumber, _appState.value.password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _appState.update {
                                    it.copy(phoneNumber = "", password = "", isLoading = true, toLoggedIn = true, accountCreationSuccess = false, toAccountCreation = true)
                                    // userData update function to real time data base
                                }
                            } else {
                                _appState.update {
                                    it.copy(
                                        alert = feedApplication.getString(R.string.problem_with_creating_an_account),
                                        isLoading = true
                                    )
                                }
                            }

                        }.addOnFailureListener { exception ->
                            val errorMessage: String = when(exception) {
                                is FirebaseAuthWeakPasswordException -> "Weak password. Please choose a stronger one"
                                is FirebaseAuthInvalidCredentialsException -> "Invalid Phone number"
                                is FirebaseAuthUserCollisionException -> "Account already exist for this phone number"
                                else -> "Problem with creation an account"
                            }
                            _appState.update {
                                it.copy(alert = errorMessage, isLoading = true)
                            }
                        }
                }

            } catch (e: Exception) {
                Log.e("NetworkRepository", "Problem with authentication", e)
                _appState.update {
                    it.copy(
                        alert = feedApplication.getString(R.string.problem_with_creating_an_account),
                        isLoading = true,
                        toLoggedIn = false,
                        accountCreationSuccess = false
                    )
                }
            }
        }
    }
    override suspend fun updateAccountDataLive() {
        withContext(Dispatchers.IO) {
            try {
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
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _appState.update {
                                    it.copy(
                                        accountCreationSuccess = true,
                                        isLoading = false,
                                        toAccountCreation = false,
                                        toLoggedIn = false,
                                        alert = feedApplication.getString(R.string.successfully_created_an_account)
                                    )
                                }
                                firebaseAuth.signOut()
                            } else {
                                _appState.update {
                                    it.copy(
                                        accountCreationSuccess = false,
                                        isLoading = true,
                                        toLoggedIn = true,
                                        toAccountCreation = true,
                                        alert = feedApplication.getString(R.string.let_s_finish_account_later)
                                    )
                                }
                            }
                        }.addOnFailureListener {
                            _appState.update {
                                it.copy(
                                    accountCreationSuccess = false,
                                    isLoading = true,
                                    toLoggedIn = true,
                                    toAccountCreation = true,
                                    alert = feedApplication.getString(R.string.let_s_finish_account_later)
                                )
                            }
                        }
                }
            } catch (e: Exception) {
                Log.e("NetworkRepository", "Problem with update account data", e)
                _appState.update {
                    it.copy(
                        accountCreationSuccess = false,
                        isLoading = true,
                        toLoggedIn = true,
                        toAccountCreation = true,
                        alert = feedApplication.getString(R.string.let_s_finish_account_later)
                    )
                }
            }
        }
    }
    override suspend fun updateAccountDataNormal() {
        withContext(Dispatchers.IO) {
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val userData = hashMapOf(
                        "userId" to currentUser.uid,
                        "firstName" to _appState.value.firstName,
                        "lastName" to _appState.value.lastName,
                        "middleName" to _appState.value.middleName,
                        "fullName" to "${_appState.value.firstName} ${_appState.value.lastName}",
                        "userName" to _appState.value.userName,
                        "profileUrl" to _appState.value.profileUrl
                    )

                    firestore.collection("userData").document(currentUser.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                            _appState.update { it.copy(toAccountCreation = true) }
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
    override suspend fun addProfileImage(uri: Uri) {
        withContext(Dispatchers.IO) {
            try {
                val currentUser = firebaseAuth.currentUser

                if (currentUser != null) {
                    val userId = currentUser.uid
                    val nameForModel = firestore.collection("userData").document(userId).get().await().getString("fullName") ?: ""
                    val userIdForModel = userId
                    // First you show set the profile url to the fireStore userData collection

                    val storageRef = fireStorage.reference.child("profile_images/$userId.jpg")
                    storageRef.putFile(uri)
                        .addOnCompleteListener {task ->
                            if (task.isSuccessful) {
                                _appState.update {
                                    it.copy(
                                        updateProfileImageWithDataBases = true,
                                        profileImageLoad = true
                                    )
                                }
                            } else {
                                _appState.update {
                                    it.copy(
                                        alert = feedApplication.getString(R.string.error_uploading_image),
                                        updateProfileImageWithDataBases = false
                                    )
                                }
                            }
                        }.addOnFailureListener { // Needs to handle exceptions here
                            _appState.update {
                                it.copy(
                                    alert = feedApplication.getString(R.string.error_uploading_image),
                                    updateProfileImageWithDataBases = false
                                )
                            }
                        }
                }
            } catch (e: Exception) {
                Log.e("NetworkRepository", "Problem with uploading profile image", e)
                _appState.update {
                    it.copy(
                        alert = feedApplication.getString(R.string.error_uploading_image),
                        updateProfileImageWithDataBases = false
                    )
                }
            }
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

                        val upDatedMap = mapOf<String, Any>(propertyName to imageUri)
                        realTimeRef.child("userData").child(userId).updateChildren(upDatedMap)
                            .addOnCompleteListener {task ->
                                if (task.isSuccessful) {
                                    _appState.update {
                                        it.copy(
                                            profileImageUrl = imageUri.toString()
                                        )
                                    }
                                } else {
                                    Log.e("NetworkRepository", "Profile image real time update failed")
                                }
                            }.addOnFailureListener { // Handle exceptions
                                Log.e("NetworkRepository", "Profile image real time update failed", it)
                            }

                        firestore.collection("userData").document(userId).update("profileImageUrl",imageUri)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful)  {
                                    _appState.update {
                                        it.copy(
                                            profileImageLoad = false,
                                            updateProfileImageWithDataBases = false
                                        )
                                    }
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
    override suspend fun writeNewPost() {
        withContext(Dispatchers.IO) {
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
                    .addOnCompleteListener {
                            task ->
                        if (task.isSuccessful) {
                            _appState.update {
                                it.copy(
                                    alert = "Posted",
                                    postCreationSuccess = true
                                )
                            }
                        }
                    }.addOnFailureListener {
                            exception ->
                        val errorMessage = when (exception) {
                            is FirebaseFirestoreException -> "Firebase realtime exception: ${exception.message ?: "Something went wrong"}"
                            else -> "Failed to create post: ${exception.message ?: "Something went wrong"}"
                        }

                        _appState.update {
                            it.copy(
                                alert = errorMessage,
                                postCreationSuccess = false
                            )
                        }

                        Log.e("AddNewPost","Firestore exception", exception)
                    }
            }
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