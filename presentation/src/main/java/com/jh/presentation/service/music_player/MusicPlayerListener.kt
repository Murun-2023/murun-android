package com.jh.presentation.service.music_player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.jh.presentation.service.music_player.MusicPlayerStateManager.updateMusicPlayerState

@UnstableApi
class MusicPlayerListener(private val onMusicEnded: () -> Unit) : Player.Listener {
    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        mediaItem?.let {
            updateMusicPlayerState {
                it.copy(currentMediaItem = mediaItem)
            }
        }
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        updateMusicPlayerState {
            it.copy(isPlaying = playWhenReady)
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == Player.STATE_ENDED) {
            onMusicEnded()
        }
    }
}