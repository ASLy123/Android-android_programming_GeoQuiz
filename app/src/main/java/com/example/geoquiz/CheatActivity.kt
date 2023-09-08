package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_cheat.*

const val EXTRA_ANSWER_SHOW = "com.bignerdranch,android.geoquiz.answer-shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
class CheatActivity : AppCompatActivity()
{
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private var answerIsTrue = false
    private val quizeViewModel: QuizViewModel by lazy{      //使用by lazy关键字，可以确保quizViewModel属性是val类型
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
        //ViewModelProvider是个注册领用ViewModel的地方。在 MainActivity首次访问QuizViewModel时，
        //ViewModelProvider会创建并返回一个QuizViewModel新实例。在设备配置改变之后，MainActivity再次访问QuizViewModel对
        //象时，它返回的是之前创建的QuizViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.txtView_answer)
        showAnswerButton = findViewById(R.id.btn_showAnswer)
        txtView_APIVersion.text = Build.VERSION.SDK_INT.toString()
        showAnswerButton.setOnClickListener {
            val answerText = if(answerIsTrue) R.string.txtTrue else R.string.txtFalse
            answerTextView.text = getString(answerText)
            quizeViewModel.isCheat = true
            setAnswerShowResult(quizeViewModel.isCheat)   //???
        }
        setAnswerShowResult(quizeViewModel.isCheat)
    }
    companion object
    {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean) : Intent
        {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
    private fun setAnswerShowResult(isAnswerShown: Boolean)
    {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOW, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }
}
