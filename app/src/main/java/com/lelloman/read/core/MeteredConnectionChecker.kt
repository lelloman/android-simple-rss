package com.lelloman.read.core

interface MeteredConnectionChecker {
    fun isNetworkMetered(): Boolean
}