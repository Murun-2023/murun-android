package com.jh.presentation.di

import com.jh.murun.domain.repository.GetMusicRepository
import com.jh.murun.domain.use_case.music.GetMusicImageUseCase
import com.jh.murun.domain.use_case.music.GetMusicByBpmUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceScopedUseCaseModule {

    @ServiceScoped
    @Provides
    fun provideGetMusicListUseCase(getMusicRepository: GetMusicRepository) = GetMusicByBpmUseCase(getMusicRepository)

    @ServiceScoped
    @Provides
    fun provideGetMusicImageUseCase(getMusicRepository: GetMusicRepository) = GetMusicImageUseCase(getMusicRepository)
}