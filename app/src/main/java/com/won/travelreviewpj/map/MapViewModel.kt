package com.won.travelreviewpj.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.record.RecordRepository
import com.won.travelreviewpj.record.diary.RecordDiary

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecordRepository()
    private val _diaries = MutableLiveData<List<RecordDiary>>()
    val diaries : LiveData<List<RecordDiary>> get() = _diaries
    suspend fun getAllRecordDiary() {
        _diaries.value = repository.getAllRecordDiaries()
    }
}