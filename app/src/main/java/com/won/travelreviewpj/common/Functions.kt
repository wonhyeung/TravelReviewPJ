package com.won.travelreviewpj.common

import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun commonToast(message: String) {
    Toast.makeText(TravelApplication.getTravelApplication(), message, Toast.LENGTH_SHORT).show()
}

fun toCoordinate(coordinate: Double) = coordinate / 1e6 / 10

fun currentTimeFormat(): String =
    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

fun currentDateFormat() = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
