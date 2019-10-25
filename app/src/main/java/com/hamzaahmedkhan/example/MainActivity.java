package com.hamzaahmedkhan.example;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hamzaahmedkhan.circulartimerview.CircularTimerListener;
import com.hamzaahmedkhan.circulartimerview.CircularTimerView;
import com.hamzaahmedkhan.circulartimerview.TimeFormatEnum;

public class MainActivity extends AppCompatActivity {

    Button btnReset;
    CircularTimerView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReset = findViewById(R.id.btnRestart);
        progressBar = findViewById(R.id.progress_circular);
        progressBar.setProgress(0);

        
        progressBar.setCircularTimerListener(new CircularTimerListener() {
            @Override
            public String updateDataOnTick(long remainingTimeInMs) {
                return String.valueOf((int)Math.ceil((remainingTimeInMs / 1000.f)));
            }

            @Override
            public void onTimerFinished() {
                Toast.makeText(MainActivity.this, "FINISHED", Toast.LENGTH_SHORT).show();
                progressBar.setPrefix("");
                progressBar.setSuffix("");
                progressBar.setText("FINISHED THANKS!");
            }
        }, 10, TimeFormatEnum.SECONDS, 10);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setProgress(0);
                progressBar.startTimer();
            }
        });
    }
}
