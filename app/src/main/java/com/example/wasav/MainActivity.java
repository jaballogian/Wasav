package com.example.wasav;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity {

    private TextView oneDayUsageTextView, sevenDayUsageTextView;
    private DatabaseReference deviceReference;
    private Double oneDayValue, sevenDayUsage, volume;
    private String timeStamp, dayFromApp, monthFromApp, yearFromApp;
    private ArrayList<String> timeStampArrayList;
    private ArrayList<Double> oneDayUsageArrayList, volumeArrayList;
    private String[] splitterMinus, splitterSlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing
        oneDayUsageTextView = (TextView) findViewById(R.id.oneDayUsageTextView);
        sevenDayUsageTextView = (TextView) findViewById(R.id.sevenDayUsageTextView);

        timeStampArrayList = new ArrayList<String>();
        oneDayUsageArrayList = new ArrayList<Double>();
        volumeArrayList = new ArrayList<Double>();

        //set font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        //get today date
        Calendar now = Calendar.getInstance();
        dayFromApp = String.valueOf(now.get(Calendar.DATE));
        monthFromApp = String.valueOf(now.get(Calendar.MONTH) + 1);
        yearFromApp = String.valueOf(now.get(Calendar.YEAR));

        Log.d("today", "day " + dayFromApp + " month " + monthFromApp + " year " + yearFromApp);

        //get data from Firebase
        deviceReference = FirebaseDatabase.getInstance().getReference().child("Devices").child("Wasav-001");
        deviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    timeStamp = ds.getKey();
                    timeStampArrayList.add(timeStamp);

                    volume = (Double) ds.getValue();
                    volumeArrayList.add(volume);

                    Log.d("testData", "time " + timeStamp + " volume " + volume);

                    oneDayValue = 0.0;
                    for(int i = 0; i < timeStampArrayList.size(); i++){

                        Log.d("compare", "dayFromApp " + dayFromApp + " splitterDay + " + splitterDay(timeStampArrayList.get(i)) +
                                " monthFromApp " + monthFromApp + " splitterMonth " + splitterMonth(timeStampArrayList.get(i)) +
                                " yearFromApp " + yearFromApp + " splitterYear " + splitterYear(timeStampArrayList.get(i)));

                        if(dayFromApp.equals(splitterDay(timeStampArrayList.get(i)))
                                && monthFromApp.equals(splitterMonth(timeStampArrayList.get(i)))
                                && yearFromApp.equals(splitterYear(timeStampArrayList.get(i)))){

                            oneDayValue = oneDayValue + volumeArrayList.get(i);
                        }
                    }
                    oneDayUsageTextView.setText(String.valueOf(oneDayValue));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //get day from Firebase
    private String splitterDay (String input){

        splitterMinus = input.split("-");
        splitterSlash = splitterMinus[0].split(":");

        String day;

        day = String.valueOf(splitterSlash[0]);
        return day;
    }

    //get month from Firebase
    private String splitterMonth (String input){

        splitterMinus = input.split("-");
        splitterSlash = splitterMinus[0].split(":");

        String month;
        if(splitterSlash[1].equals("01")){

            month = "1";
        }
        else if(splitterSlash[1].equals("02")){

            month = "2";
        }
        else if(splitterSlash[1].equals("03")){

            month = "3";
        }
        else if(splitterSlash[1].equals("04")){

            month = "4";
        }
        else if(splitterSlash[1].equals("05")){

            month = "5";
        }
        else if(splitterSlash[1].equals("06")){

            month = "6";
        }
        else if(splitterSlash[1].equals("07")){

            month = "7";
        }
        else if(splitterSlash[1].equals("08")){

            month = "8";
        }
        else if(splitterSlash[1].equals("09")){

            month = "9";
        }
        else {

            month = String.valueOf(splitterSlash[1]);
        }
        return month;
    }

    //get year from Firebase
    private String splitterYear (String input){

        splitterMinus = input.split("-");
        splitterSlash = splitterMinus[0].split(":");

        String year = "20" + String.valueOf(splitterSlash[2]);
        return year;
    }
}
