package com.lelloman.read.feed.finder

import android.os.Parcel
import android.os.Parcelable
import com.lelloman.common.utils.model.ModelWithId
import java.io.Serializable

data class FoundFeed(
    override val id: Long,
    val url: String,
    val nArticles: Int,
    var name: String? = null
) : ModelWithId, Parcelable, Serializable {

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(url)
        writeInt(nArticles)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FoundFeed> = object : Parcelable.Creator<FoundFeed> {
            override fun createFromParcel(source: Parcel): FoundFeed = FoundFeed(source)
            override fun newArray(size: Int): Array<FoundFeed?> = arrayOfNulls(size)
        }
    }
}