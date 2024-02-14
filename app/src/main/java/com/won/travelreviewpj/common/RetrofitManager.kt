package com.won.travelreviewpj.common

import com.won.travelreviewpj.travel.TravelService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit manager
 *
 * REST 연결을 위한 Singleton 연결
 */
class RetrofitManager {
    companion object {

        private var _travelService: TravelService? = null
        private val travelService get() = _travelService!!

        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        fun getRetrofitTravelService(): TravelService {
            if (_travelService != null) {
                return travelService
            } else {
                _travelService = Retrofit.Builder()
                    .baseUrl(TARGET_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(TravelService::class.java)
            }
            return travelService
        }
    }
}
