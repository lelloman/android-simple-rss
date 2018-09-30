package com.lelloman.deviceinfo.device

import android.media.AudioManager

enum class AudioMode {
    CURRENT,
    IN_CALL,
    IN_COMMUNICATION,
    NORMAL,
    INVALID,
    RINGTONE,
    UNKNOWN;

    companion object {

        fun fromAudioManagerMode(audioManagerMode: Int): AudioMode = when (audioManagerMode) {
            AudioManager.MODE_CURRENT -> CURRENT
            AudioManager.MODE_IN_CALL -> IN_CALL
            AudioManager.MODE_IN_COMMUNICATION -> IN_COMMUNICATION
            AudioManager.MODE_INVALID -> INVALID
            AudioManager.MODE_NORMAL -> NORMAL
            AudioManager.MODE_RINGTONE -> RINGTONE
            else -> UNKNOWN
        }
    }
}