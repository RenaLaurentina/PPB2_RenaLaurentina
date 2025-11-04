package com.example.ppb2_rena.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ppb2_rena.databinding.ItemTodoBinding
import com.example.ppb2_rena.entity.Todo

class TodoAdapter (
    private val dataset: MutableList<Todo>,
    private val todoItemEvents: TodoAdapter.TodoItemEvents
) : RecyclerView.Adapter<TodoAdapter.CustomViewHolder>() {

    interface TodoItemEvents {
        fun onTodoItemEdit(todo: Todo):Unit
        fun onTodoItemDelete(todo: Todo):Unit
    }

    inner class CustomViewHolder(
        val view: ItemTodoBinding
        ): RecyclerView.ViewHolder(view.root) {

            fun bindData(data: Todo) {
                view.judul.text = data.title
                view.description.text = data.description

                // eh kotlin berikan saya notofikaasi ketika element root (RelativeLayout) itu di click
                view.root.setOnClickListener {
                    // kode disini adalah aksi kita setelah mendpat nontifikasi sebuah element di click
                    todoItemEvents.onTodoItemEdit(data)
                }

                // eh kotlin berikan saya notofikaasi ketika element root (RelativeLayout) itu di click dan ditahan sekian detik
                view.root.setOnClickListener {
                    todoItemEvents.onTodoItemDelete(data)
                    true
                }
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return  CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(
        holder: CustomViewHolder,
        position: Int
    ) {
        val data = dataset[position]
        holder.bindData(data)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Todo>) {
        dataset.clear()
        dataset.addAll(newData)
        notifyDataSetChanged()
    }
}