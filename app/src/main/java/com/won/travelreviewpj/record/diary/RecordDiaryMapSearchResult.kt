package com.won.travelreviewpj.record.diary

import com.google.gson.annotations.SerializedName


data class RecordDiaryMapSearchResult(
    @SerializedName("items") val items: List<SearchItem>
)

data class SearchItem(
    @SerializedName("title") val title: String,
    @SerializedName("address") val address: String,
    @SerializedName("mapx") val mapx: String,
    @SerializedName("mapy") val mapy: String
)