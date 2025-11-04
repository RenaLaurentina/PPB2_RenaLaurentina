package com.example.ppb2_rena

import android.content.Intent
import android.icu.text.Edits
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ppb2_rena.adapter.TodoAdapter
import com.example.ppb2_rena.databinding.ActivityTaxtBinding
import com.example.ppb2_rena.entity.Todo
import com.example.ppb2_rena.usecase.TodoUsecase
import kotlinx.coroutines.launch

class TaxtActivity : AppCompatActivity() {

    private lateinit var activityBinding: ActivityTaxtBinding
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var todoUsecase: TodoUsecase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityBinding = ActivityTaxtBinding.inflate(layoutInflater)
        todoUsecase = TodoUsecase()
        setContentView(activityBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        initializeData()
        registerEvents()
    }

    fun registerEvents() {
        activityBinding.tombolTambah.setOnClickListener {
            toCreateTodoPage()
        }
    }

    fun setupRecyclerView() {
        todoAdapter = TodoAdapter(mutableListOf(), object : TodoAdapter.TodoItemEvents{
            override fun onTodoItemEdit(todo: Todo) {
                val intent = Intent(this@TaxtActivity, EditTodoActivity::class.java)
                intent.putExtra("todo_item_id", todo.id)
                startActivity(intent)
            }

            override fun onTodoItemDelete(todo: Todo) {
                val builder = AlertDialog.Builder(this@TaxtActivity)

                builder.setTitle("Konfirmasi hapus data")
                builder.setMessage("Apakah anda yakin igin menghapus data ini?")

                builder.setPositiveButton("Ya") { dialog, _ ->
                    // hapus data dari firestore
                    lifecycleScope.launch {
                        try {
                            todoUsecase.deleteTodo(todo.id)
                            initializeData()
                        } catch (exc: Exception) {
                            displayErrorMessage(exc.message)
                        }
                    }
                }

                builder.setNegativeButton("Tidak") { dialog, _ ->
                    // Kalau tidak ingin menghapus close dialog
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        })

        activityBinding.container.adapter = todoAdapter
        activityBinding.container.layoutManager = LinearLayoutManager(this)
    }

    fun initializeData() {
        activityBinding.container.visibility = View.GONE
        activityBinding.loading.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val data = todoUsecase.getTodo()
                activityBinding.container.visibility = View.VISIBLE
                activityBinding.loading.visibility = View.GONE
                todoAdapter.updateData(data)

            } catch (e: Exception) {
                activityBinding.container.visibility = View.VISIBLE
                activityBinding.loading.visibility = View.GONE
                todoAdapter.updateData(mutableListOf())
                Toast.makeText(this@TaxtActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

        fun toCreateTodoPage() {
            val intent = Intent(this,CreateTodoActivity::class.java)
            startActivity(intent)
            finish()
        }

        fun displayErrorMessage(message: String?) {
            Toast.makeText(this@TaxtActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

