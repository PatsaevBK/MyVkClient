package com.example.myvkclient.di

import android.content.Context
import com.example.myvkclient.data.network.ApiFactory
import com.example.myvkclient.data.network.ApiService
import com.example.myvkclient.data.repository.RepositoryImpl
import com.example.myvkclient.domain.repository.Repository
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindsRepository(repositoryImpl: RepositoryImpl): Repository

    companion object {

        @ApplicationScope
        @Provides
        fun providesApiService(): ApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun providesVKStorage(
            context: Context
        ): VKPreferencesKeyValueStorage {
            return VKPreferencesKeyValueStorage(context)
        }
    }
}