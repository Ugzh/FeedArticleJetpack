package com.example.feedarticlejetpack.network.dtos


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class ArticleDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "titre")
    val title: String,
    @Json(name = "descriptif")
    val description: String,
    @Json(name = "url_image")
    val urlImage: String,
    @Json(name = "categorie")
    val category: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "id_u")
    val idU: Long,
    @Json(name = "is_fav")
    val isFav: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(urlImage)
        parcel.writeInt(category)
        parcel.writeString(createdAt)
        parcel.writeLong(idU)
        parcel.writeInt(isFav)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleDto> {
        override fun createFromParcel(parcel: Parcel): ArticleDto {
            return ArticleDto(parcel)
        }

        override fun newArray(size: Int): Array<ArticleDto?> {
            return arrayOfNulls(size)
        }
    }
}