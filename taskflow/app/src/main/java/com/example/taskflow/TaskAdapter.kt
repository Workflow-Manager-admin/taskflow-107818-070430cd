package com.example.taskflow

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.CheckBox
import androidx.core.content.ContextCompat

// PUBLIC_INTERFACE
class TaskAdapter(
    val onEdit: (Task) -> Unit,
    val onDelete: (Task) -> Unit,
    val onToggleComplete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Use a MutableList so internal update is possible for list changes in place (diffing)
    private var tasks: MutableList<Task> = mutableListOf()

    // PUBLIC_INTERFACE
    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkbox: CheckBox? = view.findViewById(R.id.cb_task_completed)
        val title: TextView? = view.findViewById(R.id.tv_task_title)
        val desc: TextView? = view.findViewById(R.id.tv_task_desc)
        val editBtn: ImageButton? = view.findViewById(R.id.btn_edit)
        val deleteBtn: ImageButton? = view.findViewById(R.id.btn_delete)
    }

    // PUBLIC_INTERFACE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        Log.d("TaskAdapter", "onCreateViewHolder called for viewType=$viewType")
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        // Visual debug: Tag or change something (border is set via XML, see layout file)
        return TaskViewHolder(v)
    }

    // PUBLIC_INTERFACE
    override fun getItemCount(): Int = tasks.size

    // PUBLIC_INTERFACE
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        Log.d("TaskAdapter", "onBindViewHolder called for position=$position (itemCount=$itemCount)")
        // Defensive safety
        if (position >= tasks.size || position < 0) {
            Log.e("TaskAdapter", "onBindViewHolder: Invalid position $position, list size=${tasks.size}")
            return
        }
        val task = tasks[position]
        Log.d("TaskAdapter", "Binding task at $position: $task")

        holder.title?.text = task.title
        holder.desc?.text = task.description
        holder.checkbox?.setOnCheckedChangeListener(null)
        holder.checkbox?.isChecked = task.isCompleted
        holder.checkbox?.setOnCheckedChangeListener { _, _ -> onToggleComplete(task) }

        holder.editBtn?.setOnClickListener { onEdit(task) }
        holder.deleteBtn?.setOnClickListener { onDelete(task) }

        // Visual distinction for completed tasks
        if (task.isCompleted) {
            holder.title?.paintFlags = holder.title?.paintFlags?.or(Paint.STRIKE_THRU_TEXT_FLAG) ?: 0
        } else {
            holder.title?.paintFlags = holder.title?.paintFlags?.and(Paint.STRIKE_THRU_TEXT_FLAG.inv()) ?: 0
        }
        // Diagnostic: border/background is set in layout XML now for visual confirmation
    }

    // PUBLIC_INTERFACE
    fun submitList(list: List<Task>) {
        // Defensive copy forces RecyclerView to rebind; avoids same-object reference bug
        tasks = ArrayList(list)
        notifyDataSetChanged()
    }
}
