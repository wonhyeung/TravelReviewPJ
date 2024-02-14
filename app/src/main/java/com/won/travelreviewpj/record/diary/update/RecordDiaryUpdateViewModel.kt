package com.won.travelreviewpj.record.diary.update

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.won.travelreviewpj.record.RecordRepository
import com.won.travelreviewpj.record.diary.RecordDiary
import kotlinx.coroutines.launch
class RecordDiaryUpdateViewModel : ViewModel() {
    private val recordRepository = RecordRepository()
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