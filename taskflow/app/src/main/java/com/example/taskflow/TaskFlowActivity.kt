package com.example.taskflow

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView

// PUBLIC_INTERFACE
class TaskFlowActivity : AppCompatActivity() {

    private lateinit var pendingRecyclerView: RecyclerView
    private lateinit var completedRecyclerView: RecyclerView
    private lateinit var pendingAdapter: TaskAdapter
    private lateinit var completedAdapter: TaskAdapter
    private var tasks: MutableList<Task> = mutableListOf()
    private var nextId = 1

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taskflow)

        pendingRecyclerView = findViewById(R.id.rv_pending)
        completedRecyclerView = findViewById(R.id.rv_completed)
        val fab: FloatingActionButton = findViewById(R.id.fab_add_task)

        // Initialize Adapters
        pendingAdapter = TaskAdapter(
            onEdit = { showEditTaskDialog(it) },
            onDelete = { deleteTask(it) },
            onToggleComplete = { toggleComplete(it) }
        )
        completedAdapter = TaskAdapter(
            onEdit = { showEditTaskDialog(it) },
            onDelete = { deleteTask(it) },
            onToggleComplete = { toggleComplete(it) }
        )

        // RecyclerViews setup
        pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        completedRecyclerView.layoutManager = LinearLayoutManager(this)
        pendingRecyclerView.adapter = pendingAdapter
        completedRecyclerView.adapter = completedAdapter

        fab.setOnClickListener { showAddTaskDialog() }

        renderTaskLists()
    }

    private fun renderTaskLists() {
        val pendingTasks = tasks.filter { !it.isCompleted }
        val completedTasks = tasks.filter { it.isCompleted }
        pendingAdapter.submitList(pendingTasks)
        completedAdapter.submitList(completedTasks)
        findViewById<TextView>(R.id.tv_pending_label)?.visibility =
            if (pendingTasks.isEmpty()) View.GONE else View.VISIBLE
        findViewById<TextView>(R.id.tv_completed_label)?.visibility =
            if (completedTasks.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Task")

        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        val titleInput = EditText(this)
        titleInput.hint = "Title"
        titleInput.inputType = InputType.TYPE_CLASS_TEXT
        val descInput = EditText(this)
        descInput.hint = "Description"
        descInput.inputType = InputType.TYPE_CLASS_TEXT
        container.addView(titleInput)
        container.addView(descInput)
        builder.setView(container)

        builder.setPositiveButton("Add") { dialog, _ ->
            val rawTitle = titleInput.text.toString().trim()
            // Defensive: ensure dialog views are properly referenced even if not attached anymore
            if (rawTitle.isNotEmpty()) {
                // The following line adds a new Task to the master list
                val newTask = Task(id = nextId++, title = rawTitle, description = descInput.text.toString())
                tasks.add(newTask)
                // After adding, ensure the UI/Adapter is updated so that RecyclerView data is refreshed
                renderTaskLists()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun showEditTaskDialog(task: Task) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Task")

        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        val titleInput = EditText(this)
        titleInput.hint = "Title"
        titleInput.inputType = InputType.TYPE_CLASS_TEXT
        titleInput.setText(task.title)
        val descInput = EditText(this)
        descInput.hint = "Description"
        descInput.inputType = InputType.TYPE_CLASS_TEXT
        descInput.setText(task.description)
        container.addView(titleInput)
        container.addView(descInput)
        builder.setView(container)

        builder.setPositiveButton("Save") { dialog, _ ->
            task.title = titleInput.text.toString()
            task.description = descInput.text.toString()
            renderTaskLists()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun deleteTask(task: Task) {
        tasks.removeAll { it.id == task.id }
        renderTaskLists()
    }

    private fun toggleComplete(task: Task) {
        task.isCompleted = !task.isCompleted
        renderTaskLists()
    }
}
