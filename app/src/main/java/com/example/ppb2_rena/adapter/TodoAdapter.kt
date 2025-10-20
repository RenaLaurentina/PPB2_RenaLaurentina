package com.example.ppb2_rena.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ppb2_rena.databinding.ItemTodoBinding
import com.example.ppb2_rena.entity.Todo

class TodoAdapter (
    private val dataset: MutableList<Todo>,
    private val events: TodoItemEvents
) : RecyclerView.Adapter<TodoAdapter.CustomViewHolder>() {

    interface TodoItemEvents {
        fun onDelete(todo: Todo)
    }

    inner class CustomViewHolder(
        val view: ItemTodoBinding)
        : RecyclerView.ViewHolder(view.root) {
            fun bindData(item: Todo) {
                view.judul.text = item.title
                view.description.text = item.description
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