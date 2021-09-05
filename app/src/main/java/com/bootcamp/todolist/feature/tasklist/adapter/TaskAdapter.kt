package com.bootcamp.todolist.feature.tasklist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bootcamp.todolist.R
import com.bootcamp.todolist.feature.tasklist.model.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TaskAdapter(
    private val context: Context,
    private val list: List<Task>,
    private val onClick: ((Int) -> Unit)
) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task,parent,false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = list[position]
        with(holder.itemView){
            tv_title.text = task.title
            tv_date.text = task.date
            tv_hour.text = task.hour
            ll_item.setOnClickListener { onClick(task.id) }
        }
    }

}

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)