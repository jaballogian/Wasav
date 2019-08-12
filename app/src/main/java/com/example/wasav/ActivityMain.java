package com.example.wasav;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityMain extends AppCompatActivity {

    private TextView oneDayUsageTextView, sevenDayUsageTextView;
    private DatabaseReference deviceReference;
    private Double oneDayValue, volume, lastSevenDaysVolume;
    private String timeStamp;
    private ArrayList<String> timeStampArrayList;
    private ArrayList<Double> volumeArrayList;
    private String[] splitterMinus, splitterSlash;
    private int dayOfYearFromApp, dayOfYearLast7Days;
    private LineChartView lineChartView;
    private ArrayList yAxisValues, axisValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing
        oneDayUsageTextView = (TextView) findViewById(R.id.oneDayUsageTextView);
        sevenDayUsageTextView = (TextView) findViewById(R.id.sevenDayUsageTextView);
        lineChartView = findViewById(R.id.chart);

        timeStampArrayList = new ArrayList<String>();
        volumeArrayList = new ArrayList<Double>();
        yAxisValues = new ArrayList();
        axisValues = new ArrayList();

        //set font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        checkingUser();

        //get today date
        Calendar now = Calendar.getInstance();
        dayOfYearFromApp = now.get(Calendar.DAY_OF_YEAR);

        //get last 7 days
        Calendar last7Days = Calendar.getInstance();
        last7Days.setTime(new Date());
        last7Days.add(Calendar.DAY_OF_YEAR, -7);
        dayOfYearLast7Days = last7Days.get(Calendar.DAY_OF_YEAR);

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
                }

                oneDayValue = 0.0;
                lastSevenDaysVolume = 0.0;
                for(int i = 0; i < timeStampArrayList.size(); i++){

                    //convert timeStamp from Firebase to date format
                    SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy");
                    Date d = null;
                    try {
                        d = formatter.parse(splitterDay(timeStampArrayList.get(i)) + ":" + splitterMonth(timeStampArrayList.get(i)) + ":" + splitterYear(timeStampArrayList.get(i)));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Calendar convertedDate = Calendar.getInstance();
                    convertedDate.setTime(d);
                    int dayOfYearConvertedDate = convertedDate.get(Calendar.DAY_OF_YEAR);

                    //compare
                    if(dayOfYearConvertedDate == dayOfYearFromApp){

                        oneDayValue = oneDayValue + volumeArrayList.get(i);
                    }

                    if(dayOfYearConvertedDate > dayOfYearLast7Days && dayOfYearConvertedDate <= dayOfYearFromApp){

                        Log.d("compare", "dayOfYearLast7Days " + dayOfYearLast7Days + " dayOfYearConvertedDate " + dayOfYearConvertedDate + " dayOfYearFromApp " + dayOfYearFromApp);
                        Log.d("testVolume", String.valueOf(volumeArrayList.get(i)));
                        lastSevenDaysVolume = lastSevenDaysVolume + volumeArrayList.get(i);
                    }

                }
                oneDayUsageTextView.setText(String.valueOf(oneDayValue));
                sevenDayUsageTextView.setText(String.valueOf(lastSevenDaysVolume));

                for (int i = 0; i < timeStampArrayList.size(); i++) {
                    axisValues.add(i, new AxisValue(i).setLabel(splitterDay(timeStampArrayList.get(i)) + "/" + splitterMonth(timeStampArrayList.get(i)) + "/" + splitterYear(timeStampArrayList.get(i))));
                }

                for (int i = 0; i < volumeArrayList.size(); i++) {
                    yAxisValues.add(new PointValue(i, volumeArrayList.get(i).floatValue()));
                }

                Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

                List lines = new ArrayList();
                lines.add(line);

                LineChartData data = new LineChartData();
                data.setLines(lines);

                Axis axis = new Axis();
                axis.setValues(axisValues);
                axis.setTextSize(16);
                axis.setTextColor(Color.parseColor("#03A9F4"));
                data.setAxisXBottom(axis);

                Axis yAxis = new Axis();
                yAxis.setName("Volume in Liters");
                yAxis.setTextColor(Color.parseColor("#03A9F4"));
                yAxis.setTextSize(16);
                data.setAxisYLeft(yAxis);

                lineChartView.setLineChartData(data);
                Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                viewport.top = 110;
                lineChartView.setMaximumViewport(viewport);
                lineChartView.setCurrentViewport(viewport);

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
        if(splitterSlash[0].equals("01")){

            day = "1";
        }
        else if(splitterSlash[0].equals("02")){

            day = "2";
        }
        else if(splitterSlash[0].equals("03")){

            day = "3";
        }
        else if(splitterSlash[0].equals("04")){

            day = "4";
        }
        else if(splitterSlash[0].equals("05")){

            day = "5";
        }
        else if(splitterSlash[0].equals("06")){

            day = "6";
        }
        else if(splitterSlash[0].equals("07")){

            day = "7";
        }
        else if(splitterSlash[0].equals("08")){

            day = "8";
        }
        else if(splitterSlash[0].equals("09")){

            day = "9";
        }
        else {

            day = String.valueOf(splitterSlash[0]);
        }
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

    private void checkingUser(){

        if(FirebaseAuth.getInstance().getCurrentUser() == null){

            moveToActivityLogIn();
        }
    }

    private void moveToActivityLogIn(){

        Intent toActivityLogIn = new Intent(this, ActivityLogin.class);
        toActivityLogIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityLogIn);
        finish();
    }

}
