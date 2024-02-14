package com.won.travelreviewpj.travel

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TravelService {
    @GET("B551011/KorService1/detailCommon1?MobileOS=AND&MobileApp=TravelReviewPJ&_type=json&defaultYN=Y&firstImageYN=Y&areacodeYN=N&catcodeYN=N&addrinfoYN=Y&mapinfoYN=N&overviewYN=Y&numOfRows=1&pageNo=1")
    suspend fun getTravelData(
        @Query("serviceKey") serviceKey: String,
        @Query("contentId") contentId: Int,
    ): Response<Travel>
}

