package com.example.ppb2_rena

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ppb2_rena.databinding.ActivityCreateTodoBinding
import com.example.ppb2_rena.databinding.ActivityEditTodoBinding
import com.example.ppb2_rena.entity.Todo
import com.example.ppb2_rena.usecase.TodoUsecase
import kotlinx.coroutines.launch

class EditTodoActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityEditTodoBinding
    private lateinit var todoItemId: String
    private lateinit var todoUsecase: TodoUsecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityBinding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        todoItemId = intent.getStringExtra("todo_item_id").toString()
        todoUsecase = TodoUsecase()
        registerEvents()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun registerEvents() {
        activityBinding.tombolEdit.setOnClickListener {
            lifecycleScope.launch {
                val title = activityBinding.title.text.toString()
                val description = activityBinding.description.text.toString()
                val payload = Todo(
                    id = todoItemId,
                    title = title,
                    description = description,
                )

                try {
                    todoUsecase.updateTodo(payload)
                    displayMessage("Berhasil memperbarui data")
                    back()
                } catch (exc: Exception) {
                    displayMessage("Gagal memperbarui data task : ${exc.message}")
                }
            }
        }
    }

    fun loadData() {
        lifecycleScope.launch {
            val data = todoUsecase.getTodo(todoItemId)
            if (data == null) {
                displayMessage("Data task yang akan di edit tidak tersedia di server")
                back()
            }

            activityBinding.title.setText(data?.title)
            activityBinding.description.setText(data?.description)
        }
    }

    fun back() {
        val intent = Intent(this, TaxtActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}