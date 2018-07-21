package com.lelloman.read.persistence.db.model

import android.os.Parcel
import android.os.Parcelable
import com.lelloman.read.core.ModelWithId

data class SourceArticle(
    override val id: Long,
    val title: String,
    val subtitle: String,
    val content: String,
    val link: String,
    val imageUrl: String?,
    val time: Long,
    val sourceId: Long,
    val name: String,
    val favicon: ByteArray?
) : ModelWithId, Parcelable {

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readLong(),
        source.readString(),
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
        writeString(name)
        writeByteArray(favicon)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SourceArticle> = object : Parcelable.Creator<SourceArticle> {
            override fun createFromParcel(source: Parcel): SourceArticle = SourceArticle(source)
            override fun newArray(size: Int): Array<SourceArticle?> = arrayOfNulls(size)
        }
    }
}