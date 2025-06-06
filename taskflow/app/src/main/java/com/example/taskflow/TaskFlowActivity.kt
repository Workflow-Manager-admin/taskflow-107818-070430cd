package com.example.taskflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// PUBLIC_INTERFACE
class TaskFlowActivity : AppCompatActivity() {
    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate an extremely simple layout (built-in empty view).
        val blankView = android.widget.TextView(this)
        blankView.text = "Stub boot: TaskFlowActivity"
        setContentView(blankView)
    }
}
