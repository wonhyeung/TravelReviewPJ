package com.won.travelreviewpj.record

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties

data class Record(val name: String = "", var image: Int = 0) {

    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "image" to image
        )
    }
}