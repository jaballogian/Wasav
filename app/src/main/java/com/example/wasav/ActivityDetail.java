package com.example.wasav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityDetail extends AppCompatActivity {

    private TextView oneDayVolumeTextView, dayTextView, defaultTextView, fiveHundredsmLTextView, twoLTextView, dishTextView, fruitTextView, vegetableTextView, meatTextView, chickenTextView, fishTextView;
    private Bundle getDataFromActivityMain;
    private Double oneDayVolume, defaultValue, fiveHundredsmLValue, twoLValue, dishValue, fruitsValue, vegetableValue, meatValue, chickenValue, fishValue;;
    private int different;
    private ArrayList<String> modeArrayList;
    private ArrayList<Double> volumeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        oneDayVolumeTextView = (TextView) findViewById(R.id.oneDayVolumeTextViewActivityDetail);
        dayTextView = (TextView) findViewById(R.id.dayTextViewActivityDetail);
        defaultTextView = (TextView) findViewById(R.id.defaultTextViewActivityDetail);
        fiveHundredsmLTextView = (TextView) findViewById(R.id.fiveHundredsmLTextViewActivityDetail);
        twoLTextView = (TextView) findViewById(R.id.twoLTextViewActivityDetail);
        dishTextView = (TextView) findViewById(R.id.dishTextViewActivityDetail);
        fruitTextView = (TextView) findViewById(R.id.fruitTextViewActivityDetail);
        vegetableTextView = (TextView) findViewById(R.id.vegetableTextViewActivityDetail);
        meatTextView = (TextView) findViewById(R.id.meatTextViewActivityDetail);
        chickenTextView = (TextView) findViewById(R.id.chickenTextViewActivityDetail);
        fishTextView = (TextView) findViewById(R.id.fishTextViewActivityDetail);

        modeArrayList = new ArrayList<String>();
        volumeArrayList = new ArrayList<Double>();

        defaultValue = 0.0;
        fiveHundredsmLValue = 0.0;
        twoLValue = 0.0;
        dishValue = 0.0;
        fruitsValue = 0.0;
        vegetableValue = 0.0;
        meatValue = 0.0;
        chickenValue = 0.0;
        fishValue = 0.0;

        getDataFromActivityMain = getIntent().getExtras();
        oneDayVolume = getDataFromActivityMain.getDouble("oneDayVolume");
        different = getDataFromActivityMain.getInt("different");
        modeArrayList = getDataFromActivityMain.getStringArrayList("modeArrayList");
        volumeArrayList = (ArrayList<Double>) getIntent().getSerializableExtra("volumeArrayList");

        oneDayVolumeTextView.setText(String.valueOf(oneDayVolume) + " L");
        changeStatusDay(different);
        popoulateMode();
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

    private void popoulateMode(){

        for(int i = 0; i < modeArrayList.size(); i++){

            if(modeArrayList.get(i).equals("hand wash")){

                defaultValue = defaultValue + volumeArrayList.get(i);
            }
            else if(modeArrayList.get(i).equals("500mL")){

                fiveHundredsmLValue = fiveHundredsmLValue + volumeArrayList.get(i);
            }
            else if(modeArrayList.get(i).equals("2L")){

                twoLValue = twoLValue + volumeArrayList.get(i);
            }
            else if(modeArrayList.get(i).equals("dish")){

                dishValue = dishValue + volumeArrayList.get(i);
            }
            else if(modeArrayList.get(i).equals("fruit")){

                fruitsValue = fruitsValue + volumeArrayList.get(i);
            }
            else if(modeArrayList.get(i).equals("vegetable")){

                vegetableValue = vegetableValue + volumeArrayList.get(i);
            }
            else if(modeArrayList.get(i).equals("meat")){

                meatValue = meatValue + volumeArrayList.get(i);
            }
            else if(modeArrayList.get(i).equals("chicken")){

                chickenValue = chickenValue + volumeArrayList.get(i);
            }
            else if(modeArrayList.get(i).equals("fish")){

                fishValue = fishValue + volumeArrayList.get(i);
            }
        }

        defaultTextView.setText(defaultValue.toString() + " L");
        fiveHundredsmLTextView.setText(fiveHundredsmLValue.toString() + " L");
        twoLTextView.setText(twoLValue.toString() + " L");
        dishTextView.setText(dishValue.toString() + " L");
        fruitTextView.setText(fruitsValue.toString() + " L");
        vegetableTextView.setText(vegetableValue.toString() + " L");
        meatTextView.setText(meatValue.toString() + " L");
        chickenTextView.setText(chickenValue.toString() + " L");
        fishTextView.setText(fishValue.toString() + " L");
    }
}
