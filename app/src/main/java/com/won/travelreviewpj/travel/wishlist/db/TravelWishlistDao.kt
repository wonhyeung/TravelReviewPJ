package com.won.travelreviewpj.travel.wishlist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelWishlistDao {
    @Insert
    fun insertTravelWishlist(travelWishlist: TravelWishlist)

    @Query("SELECT * FROM wishlist_tbl WHERE id = :id")
    fun findTravelWishlist(id: Long): Flow<TravelWishlist>

    @Query(value = "DELETE FROM wishlist_tbl WHERE id = :id")
    fun deleteTravelWishlist(id: Long)

    @Query("SELECT * FROM wishlist_tbl")
    fun getAllTravelWishlist(): Flow<List<TravelWishlist>>
}
