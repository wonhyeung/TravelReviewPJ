package com.won.travelreviewpj.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.won.travelreviewpj.record.RecordRepository
import com.won.travelreviewpj.record.diary.RecordDiary

class MapViewModel : ViewModel() {
    private val repository = RecordRepository()
    val allRecordDiary: LiveData<List<RecordDiary>> = repository.getAllRecordDiaries()
}