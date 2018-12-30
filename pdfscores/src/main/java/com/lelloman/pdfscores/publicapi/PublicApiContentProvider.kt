package com.lelloman.pdfscores.publicapi

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.lelloman.pdfscores.BuildConfig
import com.lelloman.pdfscores.persistence.assets.AssetsCollectionProvider
import com.lelloman.pdfscores.persistence.db.AuthorsDao
import java.io.FileNotFoundException
import javax.inject.Inject

class PublicApiContentProvider : ContentProvider() {

    @Inject
    lateinit var authorsDao: AuthorsDao

    override fun insert(uri: Uri, values: ContentValues?) = null

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun onCreate() = true

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?) = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0

    override fun getType(uri: Uri): String? = null

    override fun openAssetFile(uri: Uri, mode: String) = when (uriMatcher.match(uri)) {
        PATH_ASSET_COLLECTION -> context!!.assets.openFd(AssetsCollectionProvider.COLLECTION_JSON_FILE_NAME)
        else -> throw FileNotFoundException("Could not find file for uri $uri")
    }

    private companion object {
        const val PATH_ASSET_COLLECTION = 1

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(BuildConfig.APPLICATION_ID + ".provider", "/asset/collection", PATH_ASSET_COLLECTION)
        }
    }
}