package com.won.travelreviewpj.travel

import android.util.Log
import androidx.core.util.toRange
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.common.SERVICE_KEY
import com.won.travelreviewpj.common.TARGET_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.random

class TravelRepository {
    val travelData: MutableLiveData<TravelEntity?> = MutableLiveData()

    companion object {

        private var _travelService: TravelService? = null
        private val travelService get() = _travelService!!

        fun getRetrofitTravelService(): TravelService {
            if (_travelService != null) {
                return travelService
            } else {
                _travelService = Retrofit.Builder()
                    .baseUrl(TARGET_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(TravelService::class.java)
            }
            return travelService
        }
    }

    suspend fun getRetrofit(): Response<Travel> {
        val contentId = (125406..125506).random()
        Log.e("contentId", "$contentId")
        return getRetrofitTravelService().getTravelData(
            serviceKey = SERVICE_KEY,
            contentId = contentId,
        )
    }

    fun getTravelData() = CoroutineScope(Dispatchers.IO).launch {
        val response = getRetrofit()
        response.let {
            if (response.isSuccessful) {
                val travelList =
                    response.body()?.response?.body?.items?.travelEntities.orEmpty().firstOrNull()
                travelData.postValue(travelList)
            }
        }
    }
}