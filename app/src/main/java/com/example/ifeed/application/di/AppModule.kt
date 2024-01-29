package com.example.ifeed.application.di

import android.content.Context
import com.example.ifeed.application.MyFeedApplication
import com.example.ifeed.data.AppState
import com.example.ifeed.data.FireNetWorkRepository
import com.example.ifeed.data.NetworkRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return  Firebase.storage("gs://i-feed-post.appspot.com")
    }

    @Provides
    @Singleton
    fun provideFirebaseDataBase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance("https://i-feed-post-default-rtdb.asia-southeast1.firebasedatabase.app/")
    }

    @Provides
    @Singleton
    fun provideAppState(): MutableStateFlow<AppState> {
        return MutableStateFlow(AppState())
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): MyFeedApplication {
        return context as MyFeedApplication
    }

//    @Provides
//    @Singleton
//    fun provideNetworkRepository(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore, firebaseStorage: FirebaseStorage, firebaseDatabase: FirebaseDatabase, appState: MutableStateFlow<AppState>, feedApplication: MyFeedApplication): NetworkRepository {
//        return FireNetWorkRepository(firebaseAuth,firebaseFirestore, firebaseStorage,firebaseDatabase,appState,feedApplication)
//    }
}
