package com.won.travelreviewpj.travel.wishlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_tbl")
data class TravelWishlist(
    @ColumnInfo(name = "wishlistTitle")
    var wishlistTitle: String = "",
    @ColumnInfo(name = "wishlistAddress")
    var wishlistAddress: String = "",
    @ColumnInfo(name = "wishlistImage")
    var wishlistImage: String = "",
    @ColumnInfo(name = "wishlistOverrview")
    var wishlistOverview: String = "",
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,
)
