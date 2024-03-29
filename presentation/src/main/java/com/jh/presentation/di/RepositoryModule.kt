package com.jh.presentation.di

import android.content.Context
import com.jh.murun.data.data_store.DataStoreManager
import com.jh.murun.data.local.MusicDao
import com.jh.murun.data.remote.ApiService
import com.jh.murun.data.remote.ResponseHandler
import com.jh.murun.data.repositoryImpl.FavoriteRepositoryImpl
import com.jh.murun.data.repositoryImpl.GetMusicRepositoryImpl
import com.jh.murun.data.repositoryImpl.SplashRepositoryImpl
import com.jh.murun.domain.repository.FavoriteRepository
import com.jh.murun.domain.repository.GetMusicRepository
import com.jh.murun.domain.repository.SplashRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideSplashRepository(dataStoreManager: DataStoreManager): SplashRepository = SplashRepositoryImpl(dataStoreManager)

    @Singleton
    @Provides
    fun provideGetMusicRepository(
        apiService: ApiService,
        responseHandler: ResponseHandler
    ): GetMusicRepository = GetMusicRepositoryImpl(apiService, responseHandler)

    @Singleton
    @Provides
    fun provideFavoriteRepository(
        musicDao: MusicDao
    ): FavoriteRepository = FavoriteRepositoryImpl(musicDao)
}