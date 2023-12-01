package com.won.travelreviewpj.record.diary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.record.Record
import com.won.travelreviewpj.record.RecordRepository

class RecordDiaryViewModel(application: Application) : AndroidViewModel(application) {
    private val recordRepository = RecordRepository()

    private val _records = MutableLiveData<Record?>()
    val records: LiveData<Record?> get() = _records

    private val _diaries = MutableLiveData<List<RecordDiary>>()
    val diaries: LiveData<List<RecordDiary>> get() = _diaries
    suspend fun getRecordDiaries(recordId: String) {
        _diaries.value = recordRepository.getRecordDiaries(recordId)
    }

    suspend fun deleteRecordDiary(recordId: String, recordDiary: String) {
        recordRepository.deleteRecordDiary(recordId, recordDiary)
        getRecordDiaries(recordId)
    }

    suspend fun findRecords(id: String?) {
        val foundRecord = recordRepository.findRecord(id)
        _records.value = foundRecord
    }


}