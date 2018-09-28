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
            AudioManager.MODE_CURRENT -> UNKNOWN
            AudioManager.MODE_IN_CALL -> UNKNOWN
            AudioManager.MODE_IN_COMMUNICATION -> UNKNOWN
            AudioManager.MODE_INVALID -> UNKNOWN
            AudioManager.MODE_NORMAL -> UNKNOWN
            AudioManager.MODE_RINGTONE -> RINGTONE
            else -> UNKNOWN
        }
    }
}