package com.won.travelreviewpj.travel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TravelViewModel(application: Application) : AndroidViewModel(application) {

    private val travelRepository = TravelRepository(application)
    private val _travelData: MutableStateFlow<TravelEntity?> = MutableStateFlow(null)
    val travelData: StateFlow<TravelEntity?> = _travelData

    fun insertTravelWishlist(travelWishlist: TravelWishlist) {
        travelRepository.insertTravelWishlist(travelWishlist)
    }

    fun getTravelData() = viewModelScope.launch {
        if (travelData.value == null) {
            travelRepository.getTravelDataResult().collect { data ->
                _travelData.value = data
            }
        }
    }

    fun getResetData() = viewModelScope.launch {
        travelRepository.getTravelDataResult().collect { data ->
            _travelData.value = data
        }
    }

}