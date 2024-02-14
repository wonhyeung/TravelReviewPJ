package com.won.travelreviewpj.record.diary

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
@IgnoreExtraProperties
data class RecordDiary(
    var id: String = "",
    val name: String = "",
    var image: String = "",
    var address: String = "",
    val companion: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val diary: String = "",
    val mapx: String = "",
    val mapy: String = "",
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "image" to image,
            "address" to address,
            "companion" to companion,
            "startDate" to startDate,
            "endDate" to endDate,
            "diary" to diary,
            "mapx" to mapx,
            "mapy" to mapy,
        )
    }
}