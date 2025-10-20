package com.example.ppb2_rena

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ppb2_rena.databinding.ActivityCreateTodoBinding
import com.example.ppb2_rena.entity.Todo
import com.example.ppb2_rena.usecase.TodoUsecase
import kotlinx.coroutines.launch

class CreateTodoActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityCreateTodoBinding
    private lateinit var todoUsecase: TodoUsecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityBinding = ActivityCreateTodoBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        todoUsecase = TodoUsecase()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        regiterEvents()
    }

    fun regiterEvents() {
        activityBinding.tombolTambah.setOnClickListener {
            saveTodoToFiresStore()
        }
    }

    fun saveTodoToFiresStore() {
        val title = activityBinding.title.text.toString()
        val description = activityBinding.description.text.toString()

        if (title == "" || description == "") {
            Toast.makeText(this@CreateTodoActivity, "Judul dan deskripsi tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        val todo = Todo(
            id = "",
            title = title,
            description = description,
        )

        lifecycleScope.launch {
            try {
                val result = todoUsecase.createTodo(todo)
                Toast.makeText(this@CreateTodoActivity, "Sukses menambah data", Toast.LENGTH_SHORT).show()
                toTodoListpage()
            } catch (exc: Exception) {
                Toast.makeText(this@CreateTodoActivity, exc.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun toTodoListpage() {
        val intent = Intent(this, TaxtActivity::class.java)
        startActivity(intent)
        finish()
    }
}





