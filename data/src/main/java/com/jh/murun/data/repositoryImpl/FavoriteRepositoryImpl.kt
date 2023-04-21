package com.jh.murun.data.repositoryImpl

import com.jh.murun.data.local.MusicDao
import com.jh.murun.domain.model.Music
import com.jh.murun.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val musicDao: MusicDao
) : FavoriteRepository {
    override suspend fun insertMusicToFavoriteList(music: Music): Flow<Boolean> {
        return flow {
            runCatching {
                musicDao.insertMusic(music)
            }.onSuccess {
                emit(true)
            }.onFailure {
                emit(false)
            }
        }
    }

    override suspend fun isMusicExistsInFavoriteList(id: String): Flow<Boolean> {
        return flow {
            runCatching {
                musicDao.readMusicId(id)
            }.onSuccess {
                when(it) {
                    true -> {
                        emit(true)
                    }
                    false -> {
                        emit(false)
                    }
                }
            }.onFailure {
                emit(false)
            }
        }
    }

    override suspend fun deleteMusicFromFavoriteList(id: String): Flow<Boolean> {
        return flow {
            runCatching {
                musicDao.deleteMusic(id)
            }.onSuccess {
                emit(true)
            }.onFailure {
                emit(false)
            }
        }
    }
}