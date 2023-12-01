package com.won.travelreviewpj.record.diary

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecordDiaryMapRepository {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(RecordDiaryMapService::class.java)

    fun searchPlaces(query: String, listener: OnSearchResultListener) {
        val call = api.searchPlaces(query)
        call.enqueue(object : Callback<RecordDiaryMapSearchResult> {
            override fun onResponse(
                call: Call<RecordDiaryMapSearchResult>,
                response: Response<RecordDiaryMapSearchResult>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    listener.onResult(result?.items ?: emptyList())
                } else {
                    listener.onError(Exception("Response is not successful"))
                }
            }

            override fun onFailure(call: Call<RecordDiaryMapSearchResult>, t: Throwable) {
                listener.onError(t)
            }
        })
    }

}