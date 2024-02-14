package com.won.travelreviewpj.travel.wishlist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.won.travelreviewpj.common.TRAVEL_WISHLIST_DATABASE
import com.won.travelreviewpj.travel.wishlist.TravelWishlist

@Database(entities = [(TravelWishlist::class)], exportSchema = false, version = 1)
abstract class TravelWishlistRoomDatabase : RoomDatabase() {
    abstract fun travelWishlistDao(): TravelWishlistDao

    companion object {
        private lateinit var INSTANCE: TravelWishlistRoomDatabase

        internal fun getDatabase(context: Context): TravelWishlistRoomDatabase {
            if (!this::INSTANCE.isInitialized) {
                synchronized(TravelWishlistRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TravelWishlistRoomDatabase::class.java,
                        TRAVEL_WISHLIST_DATABASE
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}