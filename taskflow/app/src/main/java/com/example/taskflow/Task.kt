package com.example.taskflow

// PUBLIC_INTERFACE
data class Task(
    val id: Int,
    var title: String,
    var description: String = "",
    var isCompleted: Boolean = false
)
