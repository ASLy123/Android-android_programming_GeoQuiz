package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(this.toString(),"onCreate(Bundle?) called")
        setContentView(R.layout.activity_test)
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