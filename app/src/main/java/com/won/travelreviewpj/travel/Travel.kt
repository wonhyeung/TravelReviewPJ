package com.won.travelreviewpj.travel

import com.google.gson.annotations.SerializedName

data class Travel(
    @SerializedName("response")
    val response: TravelResponse
)

data class TravelResponse(
    @SerializedName("header")
    val header: TravelHeader,
    @SerializedName("body")
    val body: TravelBody,
)

data class TravelHeader(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMsg: String
)

data class TravelBody(
    @SerializedName("items")
    val items: TravelEntityList
)

data class TravelEntityList(
    @SerializedName("item")
    val travelEntities: List<TravelEntity?>
)

data class TravelEntity(
    @SerializedName("title")
    val travelTitle: String,
    @SerializedName("firstimage")
    val travelImage: String,
    @SerializedName("addr1")
    val travelAddress: String,
    @SerializedName("overview")
    val travelOverview: String,
    @SerializedName("numOfRows")
    val numOfRows: Int
)
