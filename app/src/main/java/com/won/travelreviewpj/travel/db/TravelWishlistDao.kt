package com.won.travelreviewpj.travel.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.won.travelreviewpj.travel.wishlist.TravelWishlist

@Dao
interface TravelWishlistDao {
    @Insert
    fun insertTravelWishlist(travelWishlist: TravelWishlist)

    @Query("SELECT * FROM wishlist_tbl WHERE id = :id")
    fun findTravelWishlist(id: Long): TravelWishlist

    @Query(value = "DELETE FROM wishlist_tbl WHERE id = :id")
    fun deleteTravelWishlist(id: Long)

    @Query("SELECT * FROM wishlist_tbl")
    fun getAllTravelWishlist() : LiveData<List<TravelWishlist>>
}
