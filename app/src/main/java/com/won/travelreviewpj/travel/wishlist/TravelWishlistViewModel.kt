package com.won.travelreviewpj.travel.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.won.travelreviewpj.travel.TravelRepository

class TravelWishlistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TravelRepository = TravelRepository(application)

    fun deleteTravelWishlist(id: Long) {
        repository.deleteTravelWishlist(id)
    }

    fun getAllTravelWishlist(): LiveData<List<TravelWishlist>>? {
        return repository.allTravelWishlists
    }


}