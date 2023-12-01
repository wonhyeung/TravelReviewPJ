package com.won.travelreviewpj.travel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import kotlinx.coroutines.launch

class TravelViewModel(application: Application) : AndroidViewModel(application) {
    private val travelRepository = TravelRepository(application)
    val travelData: MutableLiveData<TravelEntity?> = travelRepository.travelData

    fun insertTravelWishlist(travelWishlist: TravelWishlist) {
        travelRepository.insertTravelWishlist(travelWishlist)
    }

    fun getTravelData() = viewModelScope.launch {
        if (travelData.value == null) {
            travelRepository.getTravelData()
        }
    }

    fun getResetData() = viewModelScope.launch {
        travelRepository.getTravelData()
    }
}