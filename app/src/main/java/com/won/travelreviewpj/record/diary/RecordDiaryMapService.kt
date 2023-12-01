package com.won.travelreviewpj.record.diary

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecordDiaryMapService {
    @Headers("X-Naver-Client-Id: cIwS9Sb0H0whgLNHQSsf", "X-Naver-Client-Secret: IZMfEpO7qU")
    @GET("v1/search/local.json")
    fun searchPlaces(
        @Query("query") query: String,
        @Query("display") display: Int = 1
    ): Call<RecordDiaryMapSearchResult>
}

interface OnSearchResultListener {
    fun onResult(items: List<SearchItem>)
    fun onError(e: Throwable)
}