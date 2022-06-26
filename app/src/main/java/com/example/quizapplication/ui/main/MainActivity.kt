package com.example.quizapplication.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.quizapplication.R
import com.example.quizapplication.databinding.ActivityMainBinding
import com.example.quizapplication.repository.Repository
import com.example.quizapplication.ui.prepare.PrepareActivity
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        onClick()
        val contents = Repository.getDataContents(this)
        Log.d("MainActivity", "onCreate:size: ${contents?.size}")
        Log.d("MainActivity", "onCreate: ${contents?.get(0)?.body}")
        Log.d("MainActivity", "onCreate: ${contents?.get(0)?.answers?.get(0)?.answer}")
    }

    private fun onClick() {
        mainBinding.btnPlay.setOnClickListener{
            startActivity<PrepareActivity>()
        }
    }
}