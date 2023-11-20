package com.won.travelreviewpj.travel.db

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import com.won.travelreviewpj.travel.wishlist.db.TravelWishlistDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TravelWishlistRepository(application: Application) {
    var searchResults = MutableLiveData<TravelWishlist>()
    private var travelWishlistDao: TravelWishlistDao
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val allTravelWishlists: LiveData<List<TravelWishlist>>?

    init {
        val db: TravelWishlistRoomDatabase = TravelWishlistRoomDatabase.getDatabase(application)
        travelWishlistDao = db.travelWishlistDao()
        allTravelWishlists = travelWishlistDao.getAllTravelWishlist()
    }

    fun insertTravelWishlist(newTravelWishList: TravelWishlist) {
        coroutineScope.launch {
            travelWishlistDao.insertTravelWishlist(newTravelWishList)
        }
    }

    fun deleteTravelWishlist(id: Long) {
        coroutineScope.launch {
            travelWishlistDao.deleteTravelWishlist(id)
        }
    }

    fun findTravelWishlist(id: Long) {
        coroutineScope.launch {
            searchResults.value = coroutineScope.async {
                return@async travelWishlistDao.findTravelWishlist(id)
            }.await()
        }
    }
}

