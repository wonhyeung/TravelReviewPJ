package com.won.travelreviewpj.record

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.won.travelreviewpj.record.diary.RecordDiary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RecordRepository {
    private val fireStoreDB = Firebase.firestore
    private val fireStoreFolderName = "folder_collection"
    private val fireStoreDiaryName = "diary_collection"

    suspend fun insertRecord(record: Record): Record {
        return withContext(Dispatchers.IO) {
            val documentRef =
                fireStoreDB.collection(fireStoreFolderName).add(record.toMap()).await()
            record.id = documentRef.id
            record
        }
    }

    suspend fun deleteRecord(record: Record) {
        withContext(Dispatchers.IO) {
            val documentRef = fireStoreDB.collection(fireStoreFolderName).document(record.id)
            documentRef.delete().await()
        }
    }

    suspend fun getRecords(): List<Record> {
        return withContext(Dispatchers.IO) {
            val documents = fireStoreDB.collection(fireStoreFolderName).get().await()
            val records = mutableListOf<Record>()
            for (document in documents) {
                val record = document.toObject(Record::class.java)
                record.id = document.id
                records.add(record)
            }
            records
        }
    }

    suspend fun findRecord(id: String?): Record? {
        return withContext(Dispatchers.IO) {
            val document =
                fireStoreDB.collection(fireStoreFolderName).document(id.toString()).get().await()
            if (document.exists()) {
                val record = document.toObject(Record::class.java)
                record?.id = document.id
                record
            } else {
                null
            }
        }
    }


    suspend fun insertRecordDiary(recordId: String, recordDiary: RecordDiary): RecordDiary {
        return withContext(Dispatchers.IO) {
            val diaryDocumentRef =
                fireStoreDB.collection(fireStoreDiaryName).add(recordDiary.toMap()).await()
            recordDiary.id = diaryDocumentRef.id
            fireStoreDB.collection(fireStoreFolderName).document(recordId)
                .update("diaryId", FieldValue.arrayUnion(diaryDocumentRef.id)).await()
            recordDiary
        }
    }

    suspend fun deleteRecordDiary(recordId: String, recordDiary: String) {

        withContext(Dispatchers.IO) {

            val diaryDocumentRef =
                fireStoreDB.collection(fireStoreDiaryName).document(recordDiary)
            diaryDocumentRef.delete().await()

            val folderDocumentRef = fireStoreDB.collection(fireStoreFolderName).document(recordId)
            folderDocumentRef.update("diaryId", FieldValue.arrayRemove(recordDiary)).await()

        }
    }

    suspend fun getRecordDiaries(recordId: String): List<RecordDiary> {
        return withContext(Dispatchers.IO) {
            val diaryList = mutableListOf<RecordDiary>()

            val recordDocument =
                fireStoreDB.collection(fireStoreFolderName).document(recordId).get().await()
            val diaryIds = recordDocument["diaryId"] as List<String>?

            diaryIds?.let { ids ->
                for (id in ids) {
                    val diaryDocument =
                        fireStoreDB.collection(fireStoreDiaryName).document(id).get().await()
                    val diary = diaryDocument.toObject(RecordDiary::class.java)

                    diary?.let {
                        it.id = diaryDocument.id
                        diaryList.add(it)
                    }
                }
            }

            diaryList
        }
    }

    suspend fun findRecordDiary(id: String): RecordDiary? {
        return withContext(Dispatchers.IO) {
            val document =
                fireStoreDB.collection(fireStoreDiaryName).document(id).get().await()
            if (document.exists()) {
                val diary = document.toObject(RecordDiary::class.java)
                diary?.id = document.id
                diary
            } else {
                null
            }
        }
    }

    suspend fun getAllRecordDiaries(): List<RecordDiary> {
        return withContext(Dispatchers.IO) {
            val documents = fireStoreDB.collection(fireStoreDiaryName).get().await()
            val diaries = mutableListOf<RecordDiary>()
            for (document in documents) {
                val diary = document.toObject(RecordDiary::class.java)
                diary.id = document.id
                diaries.add(diary)
            }
            diaries
        }
    }


}




