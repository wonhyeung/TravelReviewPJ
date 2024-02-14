package com.won.travelreviewpj.record.diary.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecordDiaryMapService {
    @Headers("X-Naver-Client-Id: Client-id", "X-Naver-Client-Secret: Secret")
    @GET("v1/search/local.json")
    fun searchPlaces(
        @Query("query") query: String,
        @Query("display") display: Int = 1
    ): Call<RecordDiaryMapSearchDto>
}

interface OnSearchResultListener {
    fun onResult(items: List<SearchItem>)
    fun onError(e: Throwable)
}