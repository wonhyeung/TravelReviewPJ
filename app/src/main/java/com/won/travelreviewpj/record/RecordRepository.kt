package com.won.travelreviewpj.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.won.travelreviewpj.common.DIARY_COLLECTION
import com.won.travelreviewpj.common.DIARY_ID
import com.won.travelreviewpj.common.FOLDER_COLLECTION
import com.won.travelreviewpj.record.diary.RecordDiary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RecordRepository {

    private val fireStoreDB = Firebase.firestore
    private val fireStoreFolderName = FOLDER_COLLECTION
    private val fireStoreDiaryName = DIARY_COLLECTION

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
                .update(DIARY_ID, FieldValue.arrayUnion(diaryDocumentRef.id)).await()
            recordDiary
        }
    }

    suspend fun deleteRecordDiary(recordId: String, recordDiary: String) {

        withContext(Dispatchers.IO) {

            val diaryDocumentRef =
                fireStoreDB.collection(fireStoreDiaryName).document(recordDiary)
            diaryDocumentRef.delete().await()

            val folderDocumentRef = fireStoreDB.collection(fireStoreFolderName).document(recordId)
            folderDocumentRef.update(DIARY_ID, FieldValue.arrayRemove(recordDiary)).await()

        }
    }

    suspend fun getRecordDiaries(recordId: String): List<RecordDiary> {
        return withContext(Dispatchers.IO) {
            val diaryList = mutableListOf<RecordDiary>()

            val recordDocument =
                fireStoreDB.collection(fireStoreFolderName).document(recordId).get().await()
            val diaryIds = recordDocument[DIARY_ID] as List<String>?

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

    fun findRecordDiary(id: String): MutableLiveData<RecordDiary?> {
        val liveData = MutableLiveData<RecordDiary?>()
        fireStoreDB.collection(fireStoreDiaryName).document(id).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val diary = documentSnapshot.toObject(RecordDiary::class.java)
                    diary?.id = documentSnapshot.id
                    liveData.value = diary
                } else {
                    liveData.value = null
                }
            }
            .addOnFailureListener { _ ->
            }
        return liveData
    }

    suspend fun updateRecordDiary(recordDiary: RecordDiary) {
        return withContext(Dispatchers.IO) {
            try {
                fireStoreDB.collection(fireStoreDiaryName).document(recordDiary.id).set(recordDiary)
                    .await()
            } catch (_: Exception) {
            }
        }
    }

    fun getAllRecordDiaries(): LiveData<List<RecordDiary>> {
        val liveData = MutableLiveData<List<RecordDiary>>()
        fireStoreDB.collection(fireStoreDiaryName).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val diaries = mutableListOf<RecordDiary>()
                    for (document in task.result) {
                        val diary = document.toObject(RecordDiary::class.java)
                        diary.id = document.id
                        diaries.add(diary)
                    }
                    liveData.value = diaries
                }
            }
        return liveData
    }
}




