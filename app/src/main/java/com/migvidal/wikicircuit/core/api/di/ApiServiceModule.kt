package com.migvidal.wikicircuit.core.api.di

import com.migvidal.wikicircuit.BuildConfig
import com.migvidal.wikicircuit.core.api.api_service.FakeOfflineApi
import com.migvidal.wikicircuit.core.api.api_service.NetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {
    @Provides
    fun providesApiService(fakeApi: FakeOfflineApi, networkApi: NetworkApi) =
        if (BuildConfig.BUILD_TYPE.contentEquals("mocked")) {
            fakeApi
        } else {
            networkApi
        }
}