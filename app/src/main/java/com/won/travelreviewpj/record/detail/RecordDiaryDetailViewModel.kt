package com.won.travelreviewpj.record.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.record.RecordRepository
import com.won.travelreviewpj.record.diary.RecordDiary

class RecordDiaryDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecordRepository()

     fun findRecordDiary(id: String): MutableLiveData<RecordDiary?> {
        return repository.findRecordDiary(id)
    }

}