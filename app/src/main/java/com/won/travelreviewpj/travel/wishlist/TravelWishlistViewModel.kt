package com.won.travelreviewpj.travel.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.travel.wishlist.db.TravelWishlistRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TravelWishlistViewModel(application: Application) : AndroidViewModel(application) {
    private val travelWishlistRepository: TravelWishlistRepository =
        TravelWishlistRepository(application)
    private val searchResults: MutableLiveData<TravelWishlist> =
        travelWishlistRepository.searchResults

    fun deleteTravelWishlist(id: Long) {
        travelWishlistRepository.deleteTravelWishlist(id)
    }

    fun findTravelWishlist(id: Long) {
        travelWishlistRepository.findTravelWishlist(id)
    }

    fun getAllTravelWishlist(): LiveData<List<TravelWishlist>>? {
        return travelWishlistRepository.allTravelWishlists
    }


}