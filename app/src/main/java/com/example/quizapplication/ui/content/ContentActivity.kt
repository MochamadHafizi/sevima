package com.example.quizapplication.ui.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapplication.R
import com.example.quizapplication.adapter.ContentAdapter
import com.example.quizapplication.databinding.ActivityContentBinding
import com.example.quizapplication.model.Content
import com.example.quizapplication.repository.Repository
import com.example.quizapplication.ui.main.MainActivity
import com.example.quizapplication.ui.score.ScoreActivity
import org.jetbrains.anko.startActivity
import java.util.ArrayList

class ContentActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_NICKNAME = "extra_nickname"
        const val EXTRA_CONTENTS = "extra_contents"
    }

    private lateinit var contentBinding: ActivityContentBinding
    private lateinit var contentAdapter: ContentAdapter
    private  lateinit var layoutManager: LinearLayoutManager
    private var dataSize = 0
    private var currentPosition = 0
    private var nickname: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentBinding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(contentBinding.root)

        //init
        contentAdapter = ContentAdapter()

        //Get Data
        if (intent != null){
            nickname = intent.getStringExtra(EXTRA_NICKNAME)
        }

        if (savedInstanceState != null){
            nickname = savedInstanceState.getString(EXTRA_NICKNAME)
            val contents = savedInstanceState.getParcelableArrayList<Content>(EXTRA_CONTENTS)
            showDataContents(contents)
        }else{
            //get data from repository
            val contents = Repository.getDataContents(this)
            //Show data
            showDataContents(contents)
        }

        //on click
        onClick()

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.are_you_sure))
            .setMessage(getString(R.string.message_exit))
            .setPositiveButton(getString(R.string.yes)){ dialog, _->
                dialog.dismiss()
                startActivity<MainActivity>()
                finishAffinity()
                super.onBackPressed()
            }
            .setNegativeButton(getString(R.string.no)){ dialog, _->
                dialog.dismiss()
            }
            .show()
    }

    private fun onClick() {
        contentBinding.btnBack.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.message_exit))
                .setPositiveButton(getString(R.string.yes)){ dialog, _->
                    dialog.dismiss()
                    startActivity<MainActivity>()
                    finishAffinity()
                }
                .setNegativeButton(getString(R.string.no)){ dialog, _->
                    dialog.dismiss()
                }
                .show()
        }

        contentBinding.btnNextContent.setOnClickListener {
            if (currentPosition < dataSize - 1){
                contentBinding.rvContent.smoothScrollToPosition(currentPosition + 1)
            }else{
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.message_input_data))
                    .setPositiveButton(getString(R.string.yes)){dialog,_->
                        val contents = contentAdapter.getResults()
                        val totalQuiz = contents.size
                        var totalCorrectAnswer = 0

                        for (content in contents){
                            for (answer in content.answers!!){
                                if (answer.isClick == true && answer.correctAnswer == true){
                                    totalCorrectAnswer += 1
                                }
                            }
                        }
                        val totalScore = totalCorrectAnswer.toDouble() / totalQuiz * 100
                        startActivity<ScoreActivity>(
                            ScoreActivity.EXTRA_NICKNAME to nickname,
                            ScoreActivity.EXTRA_SCORE to totalScore.toInt()
                        )
                        dialog.dismiss()
                    }
                    .setNegativeButton(getString(R.string.no)){dialog,_->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        contentBinding.btnPrevContent.setOnClickListener {
            contentBinding.rvContent.smoothScrollToPosition(currentPosition - 1)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_NICKNAME, nickname)
        val contents= contentAdapter.getResults()
        outState.putParcelableArrayList(EXTRA_CONTENTS, contents as ArrayList)
    }

    private fun showDataContents(contents: List<Content>?) {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()

        if (contents != null){
            contentAdapter.setData(contents as MutableList<Content>)
        }
        snapHelper.attachToRecyclerView(contentBinding.rvContent)
        contentBinding.rvContent.layoutManager = layoutManager
        contentBinding.rvContent.adapter = contentAdapter

        dataSize = layoutManager.itemCount
        contentBinding.pbContent.max = dataSize - 1

        //first show index
        var index = "${currentPosition + 1} / $dataSize"
        contentBinding.tvIndexContent.text = index

        contentBinding.rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentPosition = layoutManager.findFirstVisibleItemPosition()
                index = "${currentPosition + 1} / $dataSize"
                contentBinding.tvIndexContent.text = index
                contentBinding.pbContent.progress = currentPosition
            }
        })
    }
}