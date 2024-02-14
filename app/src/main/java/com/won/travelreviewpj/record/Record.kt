package com.won.travelreviewpj.record

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Record(
    var id: String = "", val name: String = "",
    var image: Int = 0,
    val diaryId: MutableList<String> = mutableListOf()

) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "image" to image,
            "diaryId" to diaryId
        )
    }
}