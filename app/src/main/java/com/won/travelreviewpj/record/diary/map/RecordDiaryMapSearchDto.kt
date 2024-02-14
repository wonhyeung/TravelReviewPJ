package com.won.travelreviewpj.record.diary.map


data class RecordDiaryMapSearchDto(
    val items: List<SearchItem>
)

data class SearchItem(
    val title: String,
    val address: String,
    val mapx: String,
    val mapy: String
)