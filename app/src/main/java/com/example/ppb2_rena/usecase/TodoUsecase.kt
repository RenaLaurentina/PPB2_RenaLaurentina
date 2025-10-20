package com.example.ppb2_rena.usecase

import com.example.ppb2_rena.entity.Todo
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class TodoUsecase {
    private val db : FirebaseFirestore = Firebase.firestore

    suspend fun getTodo(): List<Todo> {
        val data = db.collection("todo")
            .get()
            .await()

        if (data.isEmpty) {
            throw Exception("Data di server kosong")
        }

        return data.documents.map {
            Todo(
                id = it.id,
                title = it.get("title").toString(),
                description = it.get("deskription").toString()
            )
        }
    }

    suspend fun createTodo(todo: Todo): Todo {
        try {
            val payload = hashMapOf(
                "title" to todo.title,
                "deskription" to todo.description
            )

            val data = db.collection("todo")
                .add(payload)
                .await()

            return todo.copy(id = data.id)
        } catch (exc: Exception) {
            throw  Exception("Gagal menyimpan data ke firestore")
        }
    }
}