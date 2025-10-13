package com.example.ppb2_rena

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
    }

    private fun setupRecyclerView() {
        todoAdapter = TodoAdapter(mutableListOf())
        activityBinding.container.layoutManager = LinearLayoutManager(this)
        activityBinding.container.adapter = todoAdapter
    }

    private fun initializeData() {
        activityBinding.container.visibility = View.GONE
        activityBinding.loading.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val data = todoUsecase.getTodo()
                Log.d("info app", data.toString())
                activityBinding.container.visibility = View.VISIBLE
                activityBinding.loading.visibility = View.GONE
                todoAdapter.updateData(data)
            } catch (e: Exception) {
                activityBinding.loading.visibility = View.GONE
                e.printStackTrace()
            }
        }
    }
}
