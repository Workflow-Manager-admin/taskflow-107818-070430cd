package com.example.taskflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

// PUBLIC_INTERFACE
class TaskFlowActivity : AppCompatActivity() {

    private val tasks: MutableList<Task> = mutableListOf()
    private var nextId: Int = 1

    private lateinit var pendingAdapter: TaskAdapter
    private lateinit var completedAdapter: TaskAdapter

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taskflow)

        val pendingRecyclerView = findViewById<RecyclerView>(R.id.rv_pending)
        val completedRecyclerView = findViewById<RecyclerView>(R.id.rv_completed)
        val fabAddTask = findViewById<FloatingActionButton>(R.id.fab_add_task)

        // Defensive null check for views
        if (pendingRecyclerView == null || completedRecyclerView == null || fabAddTask == null) {
            Toast.makeText(this, "Error loading key components. Please restart the app.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        pendingAdapter = TaskAdapter(
            { task -> if (task != null) editTask(task) },
            { task -> if (task != null) deleteTask(task) },
            { task -> if (task != null) toggleTaskCompleted(task) }
        )
        completedAdapter = TaskAdapter(
            { task -> if (task != null) editTask(task) },
            { task -> if (task != null) deleteTask(task) },
            { task -> if (task != null) toggleTaskCompleted(task) }
        )
        pendingRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TaskFlowActivity)
            adapter = pendingAdapter
        }
        completedRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TaskFlowActivity)
            adapter = completedAdapter
        }

        fabAddTask.setOnClickListener {
            addTask()
        }

        refreshTaskLists()
    }

    // PUBLIC_INTERFACE
    private fun addTask() {
        // Stub: You would show a dialog to enter details.
        val demoTask = Task(
            id = nextId++,
            title = "New Task $nextId",
            description = "Description...",
            isCompleted = false
        )
        tasks.add(demoTask)
        refreshTaskLists()
        Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()
    }

    // PUBLIC_INTERFACE
    private fun editTask(task: Task) {
        // Stub: You would open an edit dialog here.
        Toast.makeText(this, "Edit Task: ${task.title}", Toast.LENGTH_SHORT).show()
    }

    // PUBLIC_INTERFACE
    private fun deleteTask(task: Task) {
        tasks.removeAll { it.id == task.id }
        refreshTaskLists()
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
    }

    // PUBLIC_INTERFACE
    private fun toggleTaskCompleted(task: Task) {
        task.isCompleted = !task.isCompleted
        refreshTaskLists()
    }

    // PUBLIC_INTERFACE
    private fun refreshTaskLists() {
        val pending = tasks.filter { !it.isCompleted }
        val completed = tasks.filter { it.isCompleted }
        pendingAdapter.submitList(pending)
        completedAdapter.submitList(completed)
    }
}
