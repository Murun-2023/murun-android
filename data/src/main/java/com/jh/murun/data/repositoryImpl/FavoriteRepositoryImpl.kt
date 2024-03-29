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

    override fun readAllMusics(): Flow<List<Music>?> {
        return flow {
            runCatching {
                musicDao.readAllMusic()
            }.onSuccess {
                emit(it)
            }.onFailure {
                emit(null)
            }
        }
    }

    override fun insertMusicToFavoriteList(music: Music): Flow<Boolean> {
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

    override fun deleteMusicFromFavoriteList(music: Music): Flow<Boolean> {
        return flow {
            runCatching {
                musicDao.deleteMusic(music.id)
            }.onSuccess {
                emit(true)
            }.onFailure {
                emit(false)
            }
        }
    }

    override suspend fun updateReorderedFavoriteList(musicList: List<Music>) {
        try {
            musicList.forEach { music ->
                music.newIndex?.let {
                    musicDao.updateNewIndex(
                        newIndex = it,
                        id = music.id
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}