package com.won.travelreviewpj.travel.wishlist.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.travel.TravelRepository
import com.won.travelreviewpj.travel.wishlist.TravelWishlist

class TravelWishlistDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TravelRepository = TravelRepository(application)
    private val searchResult: MutableLiveData<TravelWishlist> = repository.searchResults

    fun findWishlist(id: Long) {
        repository.findTravelWishlist(id)
    }
    fun getSearchResults(): MutableLiveData<TravelWishlist> {
        return searchResult
    }
}