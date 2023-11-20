package com.won.travelreviewpj.travel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import com.won.travelreviewpj.travel.wishlist.db.TravelWishlistRepository
import kotlinx.coroutines.launch

class TravelViewModel(application: Application) : AndroidViewModel(application) {
    private val travelRepository = TravelRepository()
    val travelData: MutableLiveData<TravelEntity?> = travelRepository.travelData
    private val travelWishlistRepository: TravelWishlistRepository = TravelWishlistRepository(application)
    private val searchResults: MutableLiveData<TravelWishlist> = travelWishlistRepository.searchResults

    fun insertTravelWishlist(travelWishlist: TravelWishlist) {
        travelWishlistRepository.insertTravelWishlist(travelWishlist)
    }
    fun getTravelData() = viewModelScope.launch {
        if(travelData.value == null) {
            travelRepository.getTravelData()
        }
    }
    fun getResetData() = viewModelScope.launch {
        travelRepository.getTravelData()
    }
}