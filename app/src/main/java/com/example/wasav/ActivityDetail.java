package com.example.wasav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

public class ActivityDetail extends AppCompatActivity {

    private TextView oneDayVolumeTextView, dayTextView;
    private Bundle getDataFromActivityMain;
    private Double oneDayVolume;
    private int different;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        oneDayVolumeTextView = (TextView) findViewById(R.id.oneDayVolumeTextViewActivityDetail);
        dayTextView = (TextView) findViewById(R.id.dayTextViewActivityDetail);
        getDataFromActivityMain = getIntent().getExtras();
        oneDayVolume = getDataFromActivityMain.getDouble("oneDayVolume");
        different = getDataFromActivityMain.getInt("different");

        oneDayVolumeTextView.setText(String.valueOf(oneDayVolume) + " L");
        changeStatusDay(different);

    }

    private void changeStatusDay(int input){

        if(input == 0){

            dayTextView.setText("Today's Usage");
        }
        else if(input == 1){

            dayTextView.setText("Yesterday's Usage");
        }
        else if(input >= 2){

            dayTextView.setText("Past's Usage");
        }
        else if(input == -1){

            dayTextView.setText("Tomorrow's Usage");
        }
        else if(input <= -2){

            dayTextView.setText("Future's Usage");
        }
    }
}
