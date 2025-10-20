package com.example.ppb2_rena

import android.content.Intent
import android.icu.text.Edits
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
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
        todoAdapter = TodoAdapter(mutableListOf(), object : TodoAdapter.TodoItemEvents {
            override fun onDelete(todo: Todo) {}
        })

        activityBinding.container.adapter = todoAdapter
        activityBinding.container.layoutManager = LinearLayoutManager(this)
    }

    fun initializeData() {
        activityBinding.container.visibility = View.GONE
        activityBinding.loading.visibility = View.VISIBLE

        lifecycleScope.launch {
            val data = todoUsecase.getTodo()
            activityBinding.container.visibility = View.VISIBLE
            activityBinding.loading.visibility = View.GONE
            todoAdapter.updateData(data)
        }
    }

        fun toCreateTodoPage() {
            val intent = Intent(this,CreateTodoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

