package com.example.ppb2_rena.usecase

import com.example.ppb2_rena.entity.Todo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class TodoUsecase {
    val db = Firebase.firestore

    suspend fun getTodo(): List<Todo> {
        return try {
            val snapshot = db.collection("todo")
                .get()
                .await()

            snapshot.documents.map { document ->
                Todo(
                    id = document.id,
                    title = document.getString("title") ?: "",
                    description = document.getString("description") ?: ""
                )
            }
        } catch (exc: Exception) {

            throw Exception("Gagal mengambil data todo: ${exc.message}")
        }
    }
}