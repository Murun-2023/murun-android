package com.jh.presentation.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.jh.murun.domain.model.Music
import com.jh.murun.domain.model.ResponseState
import com.jh.murun.domain.use_case.music.GetMusicByIdUseCase
import com.jh.murun.domain.use_case.music.GetMusicFileUseCase
import com.jh.murun.domain.use_case.music.GetMusicImageUseCase
import com.jh.murun.domain.use_case.music.GetMusicListByCadenceUseCase
import com.jh.presentation.di.IoDispatcher
import com.jh.presentation.di.MainDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MusicLoaderService : Service() {
    @Inject
    lateinit var getMusicByIdUseCase: GetMusicByIdUseCase

    @Inject
    lateinit var getMusicListByCadenceUseCase: GetMusicListByCadenceUseCase

    @Inject
    lateinit var getMusicFileUseCase: GetMusicFileUseCase

    @Inject
    lateinit var getMusicImageUseCase: GetMusicImageUseCase

    @Inject
    @MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    @IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    private val _completeMusicFlow: MutableSharedFlow<Music> = MutableSharedFlow()
    val completeMusicFlow: SharedFlow<Music>
        get() = _completeMusicFlow

    private val musicQueue: Queue<Music> = LinkedList()

    inner class MusicLoaderServiceBinder : Binder() {
        fun getServiceInstance(): MusicLoaderService {
            return this@MusicLoaderService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return MusicLoaderServiceBinder()
    }

    fun loadMusicListByCadence(cadence: Int) {
        CoroutineScope(ioDispatcher).launch {
            getMusicListByCadenceUseCase(cadence = cadence).onEach { result ->
                when (result) {
                    is ResponseState.Success -> {
                        musicQueue.clear()
                        musicQueue.addAll(
                            listOf(
                                result.data.first(),
                                Music(
                                    id = "",
                                    artist = "d",
                                    imageUrl = "https://coil-kt.github.io/coil/logo.svg",
                                    fileUrl = "https://cdn.pixabay.com/download/audio/2023/03/26/audio_87449b1afe.mp3?filename=mortal-gaming-144000.mp3",
                                    title = "타이틀"
                                )
                            )
                        )

                        if (musicQueue.isNotEmpty()) {
                            loadMusicFileAndImage(musicQueue.poll()!!)
                        }
                    }
                    is ResponseState.Error -> {} // TODO : Error handling
                }
            }.collect()
        }
    }

    private fun loadMusicFileAndImage(music: Music) {
        if (music.fileUrl != null && music.imageUrl != null) {
            CoroutineScope(ioDispatcher).launch {
                getMusicFileUseCase(music.fileUrl!!).zip(getMusicImageUseCase(music.imageUrl!!)) { musicFile, musicImage ->
                    if (musicFile != null && musicImage != null) {
                        Pair(writeMusicFileToDisk(musicFile.byteStream(), music.title), musicImage.bytes())
                    } else {
                        null
                    }
                }.onEach { result ->
                    _completeMusicFlow.emit(
                        music.apply {
                            diskPath = result?.first
                            image = result?.second
                        }
                    )
                }.collect()
            }
        }
    }

    private suspend fun writeMusicFileToDisk(byteStream: InputStream, title: String): String {
        var path = ""
        try {
            val file = File(applicationContext.cacheDir, "$title.mp3")
            val byteArray = ByteArray(4096)
            var fileSizeDownloaded = 0
            withContext(ioDispatcher) {
                val outputStream = FileOutputStream(file)

                while (true) {
                    val read = byteStream.read(byteArray)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(byteArray, 0, read)
                    fileSizeDownloaded += read
                    outputStream.flush()
                }
            }

            path = file.absolutePath
        } catch (e: Exception) {
            println(e)
        }

        return path
    }

    fun loadNextMusicFile() {
        if (musicQueue.isNotEmpty()) {
//            loadMusicFile(musicQueue.poll()!!)
        } else {
            // TODO : NoSuchException Handling
        }
    }
}