package com.won.travelreviewpj.travel

import com.won.travelreviewpj.common.SERVICE_KEY
import com.won.travelreviewpj.common.TARGET_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TravelRepository {

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
        return getRetrofitTravelService().getTravelData(
            serviceKey = SERVICE_KEY,
            contentId = 126508
        )
    }
}