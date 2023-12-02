package com.won.travelreviewpj.record.update

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.won.travelreviewpj.record.Record
import com.won.travelreviewpj.record.RecordRepository
import com.won.travelreviewpj.record.diary.RecordDiary
import kotlinx.coroutines.launch

class RecordDiaryUpdateViewModel(application: Application) : AndroidViewModel(application) {
    private val recordRepository = RecordRepository()

    private val _diary = MutableLiveData<List<Record>>()
    val diary: LiveData<List<Record>> get() = _diary

    var scheduleStart: String = "00년 00월 00일"
    var scheduleEnd: String = "00년 00월 00일"

    suspend fun insertRecordDiary(recordId: String, recordDiary: RecordDiary) {
        recordRepository.insertRecordDiary(recordId, recordDiary)
    }

    fun findRecordDiary(id: String): MutableLiveData<RecordDiary?> {
        return recordRepository.findRecordDiary(id)
    }

     fun updateRecordDiary(recordDiary: RecordDiary) {
        viewModelScope.launch {
            recordRepository.updateRecordDiary(recordDiary)
        }
    }


}