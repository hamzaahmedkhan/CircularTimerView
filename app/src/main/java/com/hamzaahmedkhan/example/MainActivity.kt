package com.hamzaahmedkhan.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.hamzaahmedkhan.circulartimerview.CircularTimerListener
import com.hamzaahmedkhan.circulartimerview.CircularTimerView
import com.hamzaahmedkhan.circulartimerview.TimeFormatEnum

class MainActivity : AppCompatActivity() {

    lateinit var btnReset: Button
    lateinit var progressBar: CircularTimerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnReset = findViewById(R.id.btnRestart)
        progressBar = findViewById(R.id.progress_circular)
        progressBar.progress = 0f


        progressBar.setCircularTimerListener(object : CircularTimerListener {
            override fun updateDataOnTick(remainingTimeInMs: Long): String {
                return Math.ceil((remainingTimeInMs / 1000f).toDouble()).toInt().toString()
            }

            override fun onTimerFinished() {
                Toast.makeText(this@MainActivity, "FINISHED", Toast.LENGTH_SHORT).show()
                progressBar.prefix = ""
                progressBar.suffix = ""
                progressBar.text = "FINISHED THANKS!"
            }
        }, 10, TimeFormatEnum.SECONDS, 10)


        btnReset.setOnClickListener {
            progressBar.progress = 0f
            progressBar.startTimer()
        }
    }
}
