package com.example.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import com.example.geoquiz.REQUEST_CODE_CHEAT as REQUEST_CODE_CHEAT

private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0
class MainActivity : AppCompatActivity()
{

    private var score = 0
    private val quizeViewModel: QuizViewModel by lazy{      //使用by lazy关键字，可以确保quizViewModel属性是val类型
            ViewModelProviders.of(this).get(QuizViewModel::class.java)

        //ViewModelProvider是个注册领用ViewModel的地方。在 MainActivity首次访问QuizViewModel时，
        //ViewModelProvider会创建并返回一个QuizViewModel新实例。在设备配置改变之后，MainActivity再次访问QuizViewModel对
        //象时，它返回的是之前创建的QuizViewModel
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle)
    {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(this.toString(),"onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizeViewModel.currentIndex)
    }

    private fun updateQuestion()
    {
        val questionTextResId = quizeViewModel.currentQuestionText
        quizeViewModel.isCheat = false
        textView_question.setText(questionTextResId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {

        super.onActivityResult(requestCode, resultCode, data)
//            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHEAT)
//            {
//                quizeViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOW, false) ?: false
//            }
        when(requestCode)
        {
            REQUEST_CODE_CHEAT  ->
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    quizeViewModel.isCheat = data?.getBooleanExtra(EXTRA_ANSWER_SHOW, false) ?: false
                }
            }
        }
    }

    private fun checkAnswer(userAnswer: Boolean)
    {
        val messageResId = when
        {
            quizeViewModel.isCheat ->
            {
                R.string.judgment_toast
            }
            userAnswer == quizeViewModel.currentQuestionAnswer ->
            {
                score += 1
                R.string.txtTrue
            }
            else -> R.string.txtFalse
        }
        Toast.makeText(this, "您回答" + getString(messageResId), Toast.LENGTH_SHORT).show()
    }

    private fun printScore()
    {
        if(quizeViewModel.currentIndex % 6 == 0)
        {
            Toast.makeText(this,"您的正确率是：" + (score * 100 / 6)  + "%",Toast.LENGTH_SHORT).show()
            score = 0
        }
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.d(this.toString(),"onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizeViewModel.currentIndex = currentIndex
//        val provider : ViewModelProvider = ViewModelProviders.of(this)
        //创建并返回一个关联了 MainActivity的ViewModelProvider实例
//        val quizViewModel = provider.get(QuizViewModel::class.java)
        //返回一个 QuizViewModel实例。
//        Log.d(this.toString(), "Got a QuizViewModel: $quizViewModel")

        btn_false.setOnClickListener {
            checkAnswer(false)
            quizeViewModel.moveToNext()
            updateQuestion()
            printScore()
        }

        btn_true.setOnClickListener {
            checkAnswer(true)
            quizeViewModel.moveToNext()
            updateQuestion()
            printScore()
        }

        btn_cheat.setOnClickListener {
            val answerIsTrue = quizeViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && quizeViewModel.cheatCnt < 3)
            {
                val options = ActivityOptions.makeClipRevealAnimation(it, 0, 0, it.width, it.height)
                //ActivityOptions类来定制该如何启动 activity
                //调用makeClipRevealAnimation(...)可以让 CheatActivity出现时带动画效果
                //makeClipRevealAnimation(...)中的参数值指定了视图动画对 象（这里是指CHEAT!按钮）、显示新activity位置的x 和y 坐标
                // （相 对于动画源对象），以及新activity的初始高宽值

                quizeViewModel.cheatCnt += 1
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
                //请求代码是先发送给子activity，然后再返回给父activity的整数值，由用户定义。
                // 在一个activity启动多个不同类型的子activity且需要判 断消息回馈方时，就会用到该请求代码。
                //调用options.toBundle()把ActivityOptions信息打包 到Bundle对象里，
                //然后传给startActivityForResult(...)。 随后，ActivityManager就知道该如何展现你的activity了


            }
            else if (quizeViewModel.cheatCnt < 3)
            {
                quizeViewModel.cheatCnt += 1
                startActivityForResult(intent, REQUEST_CODE_CHEAT,)
            }
            Toast.makeText(this, "您剩下提示的次数是：" + (3 - quizeViewModel.cheatCnt), Toast.LENGTH_SHORT).show()
        }


        imgBtn_next.setOnClickListener {
            quizeViewModel.moveToNext()
            updateQuestion()
        }
        imgBtn_back.setOnClickListener {
            quizeViewModel.moveToPre()
            updateQuestion()
        }
        btn_test.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }
        updateQuestion()

    }



    override fun onStart() {
        super.onStart()
        Log.d(this.toString(), "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(this.toString(), "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(this.toString(), "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(this.toString(), "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(this.toString(), "onDestroy() called")
    }
}