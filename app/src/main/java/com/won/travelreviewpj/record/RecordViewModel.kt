package com.won.travelreviewpj.record

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecordViewModel(application: Application) : AndroidViewModel(application) {

    private val recordRepository = RecordRepository()
    suspend fun insertRecord(record: Record) {
        recordRepository.insertRecord(record)
    }

    suspend fun getRecords(): List<Record> = withContext(Dispatchers.IO) {
        recordRepository.getRecords()
    }

}