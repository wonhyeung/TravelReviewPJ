package com.won.travelreviewpj.record.diary.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.won.travelreviewpj.record.RecordRepository
import com.won.travelreviewpj.record.diary.RecordDiary

class RecordDiaryDetailViewModel : ViewModel() {
    private val repository = RecordRepository()

    fun findRecordDiary(id: String): MutableLiveData<RecordDiary?> {
        return repository.findRecordDiary(id)
    }
}