package com.lelloman.read.core.navigation

import android.net.Uri

class DeepLink(
    val screen: NavigationScreen,
    val parameters: Map<String, Any> = emptyMap()
) {

    private val stringRepresentation by lazy {
        val builder = Uri
            .Builder()
            .scheme(PROTOCOL)
            .appendPath(screen.name)

        getParametersList().forEach {
            builder.appendQueryParameter(it.first, it.second.toString())
        }
        builder.build().toString()
    }

    private fun getParametersList() = parameters
        .entries
        .toList()
        .map { it.key to it.value }
        .sortedBy { (key, _) -> key }

    override fun toString() = stringRepresentation

    companion object {
        const val PROTOCOL = "smurfs"
    }
}