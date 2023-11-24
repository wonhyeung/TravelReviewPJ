package com.won.travelreviewpj.record

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RecordRepository {
    private val fireStoreDB = Firebase.firestore
    private val fireStoreCollectionName = "folder_collection"

    suspend fun insertRecord(record: Record) {
        val documentRef = fireStoreDB.collection(fireStoreCollectionName)
        documentRef.add(record.toMap()).await()
    }

    suspend fun getRecords(): List<Record> = withContext(Dispatchers.IO) {
        fireStoreDB.collection(fireStoreCollectionName)
            .orderBy(FieldPath.documentId(), Query.Direction.ASCENDING)
            .get().await().toObjects(Record::class.java)
    }
}