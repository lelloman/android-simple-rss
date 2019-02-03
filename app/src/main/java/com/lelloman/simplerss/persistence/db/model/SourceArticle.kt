package com.lelloman.simplerss.persistence.db.model

import android.os.Parcel
import android.os.Parcelable
import com.lelloman.common.utils.model.ModelWithId

@Suppress("ArrayInDataClass")
data class SourceArticle(
    override val id: Long,
    val title: String,
    val subtitle: String,
    val content: String,
    val link: String,
    val imageUrl: String?,
    val time: Long,
    val sourceId: Long,
    val sourceName: String,
    val favicon: ByteArray?
) : ModelWithId<Long>, Parcelable {

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString(),
        source.readLong(),
        source.readLong(),
        source.readString()!!,
        source.createByteArray()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(title)
        writeString(subtitle)
        writeString(content)
        writeString(link)
        writeString(imageUrl)
        writeLong(time)
        writeLong(sourceId)
        writeString(sourceName)
        writeByteArray(favicon)
    }

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<com.lelloman.simplerss.persistence.db.model.SourceArticle> = object : Parcelable.Creator<com.lelloman.simplerss.persistence.db.model.SourceArticle> {
            override fun createFromParcel(source: Parcel): com.lelloman.simplerss.persistence.db.model.SourceArticle = com.lelloman.simplerss.persistence.db.model.SourceArticle(source)
            override fun newArray(size: Int): Array<com.lelloman.simplerss.persistence.db.model.SourceArticle?> = arrayOfNulls(size)
        }
    }
}