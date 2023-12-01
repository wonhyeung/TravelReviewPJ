package com.won.travelreviewpj.record

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RecordViewModel(application: Application) : AndroidViewModel(application) {

    private val recordRepository = RecordRepository()
    private val _records = MutableLiveData<List<Record>>()
    val records: LiveData<List<Record>> get() = _records

    suspend fun insertRecord(record: Record): Record {
        return recordRepository.insertRecord(record)
    }

    suspend fun getRecords() {
        _records.value = recordRepository.getRecords()
    }

    suspend fun deleteRecord(record: Record) {
        recordRepository.deleteRecord(record)
        getRecords()
    }

}