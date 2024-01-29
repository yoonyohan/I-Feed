package com.example.ifeed.application.di

import com.example.ifeed.data.FireNetWorkRepository
import com.example.ifeed.data.NetworkRepository
import com.google.android.datatransport.runtime.dagger.Binds
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @dagger.Binds
    @Singleton
    abstract fun bindNetworkRepository(
        fireNetWorkRepository: FireNetWorkRepository
    ): NetworkRepository
}