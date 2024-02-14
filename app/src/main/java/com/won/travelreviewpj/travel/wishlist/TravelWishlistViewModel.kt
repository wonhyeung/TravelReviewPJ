package com.won.travelreviewpj.travel.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.won.travelreviewpj.travel.TravelRepository
import kotlinx.coroutines.flow.Flow

class TravelWishlistViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TravelRepository = TravelRepository(application)

    fun deleteTravelWishlist(id: Long) {
        repository.deleteTravelWishlist(id)
    }

    fun getAllTravelWishlist(): Flow<List<TravelWishlist>>? {
        return repository.allTravelWishlists
    }

}