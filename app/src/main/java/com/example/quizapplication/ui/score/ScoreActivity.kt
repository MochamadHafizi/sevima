package com.example.quizapplication.ui.score

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizapplication.R
import com.example.quizapplication.databinding.ActivityScoreBinding
import com.example.quizapplication.ui.main.MainActivity
import org.jetbrains.anko.startActivity

class ScoreActivity : AppCompatActivity() {
    private lateinit var scoreBinding: ActivityScoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scoreBinding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(scoreBinding.root)

        onClick()
    }

    private fun onClick() {
        scoreBinding.btnDone.setOnClickListener{
            startActivity<MainActivity>()
            finishAffinity()
        }
    }
}