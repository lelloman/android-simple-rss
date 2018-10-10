package com.lelloman.launcher.classification

interface IdentifierEncoderProvider {
    fun provideEncoder(identifiers: List<String>): IdentifierEncoder
}