package com.won.travelreviewpj.record.diary.map

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Map retrofit manager
 *
 * naver search API 지도 정보 검색
 */
class MapRetrofitManager {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(RecordDiaryMapService::class.java)

    fun searchPlaces(query: String, listener: OnSearchResultListener) {
        val call = api.searchPlaces(query)
        call.enqueue(object : Callback<RecordDiaryMapSearchDto> {
            override fun onResponse(
                call: Call<RecordDiaryMapSearchDto>,
                response: Response<RecordDiaryMapSearchDto>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    listener.onResult(result?.items ?: emptyList())
                }
            }
            override fun onFailure(call: Call<RecordDiaryMapSearchDto>, t: Throwable) {
                listener.onError(t)
            }
        })
    }

}