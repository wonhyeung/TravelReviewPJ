package com.won.travelreviewpj.travel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.won.travelreviewpj.common.SERVICE_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TravelViewModel() : ViewModel() {
    val travelData: MutableLiveData<TravelEntity?> = MutableLiveData()
    private val travelRepository = TravelRepository()
    fun getTravelData() = viewModelScope.launch(Dispatchers.IO) {
        val response = travelRepository.getRetrofit()
        response.let {
            if (response.isSuccessful) {
                val travelList =
                    response.body()?.response?.body?.items?.travelEntities.orEmpty().firstOrNull()
                travelData.postValue(travelList)
            }
        }
    }

}