package com.lelloman.pdfscores.publicapi

import android.content.ContentProvider
import android.content.ContentValues
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import com.lelloman.pdfscores.persistence.db.AuthorsDao
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

    override fun getType(uri: Uri): String?  = null

    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        return super.openAssetFile(uri, mode)
    }
}