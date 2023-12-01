package com.won.travelreviewpj.record.update

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.record.Record
import com.won.travelreviewpj.record.RecordRepository
import com.won.travelreviewpj.record.diary.RecordDiary

class RecordDiaryUpdateViewModel(application: Application) : AndroidViewModel(application) {
    private val recordRepository = RecordRepository()

    var scheduleStart : String ="00년 00월 00일"
    var scheduleEnd : String = "00년 00월 00일"

    suspend fun insertRecordDiary(recordId: String, recordDiary: RecordDiary) {
        recordRepository.insertRecordDiary(recordId, recordDiary)
    }

}