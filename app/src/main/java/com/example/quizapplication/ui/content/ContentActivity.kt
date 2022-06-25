package com.example.quizapplication.ui.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizapplication.R
import com.example.quizapplication.databinding.ActivityContentBinding

class ContentActivity : AppCompatActivity() {

    private lateinit var contentBinding: ActivityContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentBinding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(contentBinding.root)
    }
}