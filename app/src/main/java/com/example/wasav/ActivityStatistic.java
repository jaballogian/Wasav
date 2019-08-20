package com.example.wasav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ActivityStatistic extends AppCompatActivity {

    private RelativeLayout homeRelativeLayout, profileRelativeLayout, minusRelativeLayout, plusRelativeLayout;
    private TextView dateTextView, totalUsageTextView;
    private LineChartView lineChartView;
//    private Bundle readDataFromActivityMain;
    private ArrayList<String> timeStampArrayList, newtimeStampArrayList;
    private ArrayList yAxisValues, axisValues;
    private String[] splitterMinus, splitterSlash;
    private ArrayList<Double> volumeArrayList, newVolumeArrayList;
    private int plusMinusWeeks;
    private Calendar newDate;
    private DatabaseReference deviceReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        homeRelativeLayout = (RelativeLayout) findViewById(R.id.homeRelativeLayoutActivityStatistic);
        profileRelativeLayout = (RelativeLayout) findViewById(R.id.profileRelativeLayoutActivityStatistic);
        minusRelativeLayout = (RelativeLayout) findViewById(R.id.minusRelativeLayoutActivityStatistic);
        plusRelativeLayout = (RelativeLayout) findViewById(R.id.plusRelativeLayoutActivityStatistic);
        dateTextView = (TextView) findViewById(R.id.dateTextViewActivityStatistic);
        lineChartView = (LineChartView) findViewById(R.id.chartActvityStatistic);
        totalUsageTextView = (TextView) findViewById(R.id.totalUsageTextViewActivityStatistic);
//        readDataFromActivityMain = getIntent().getExtras();

        timeStampArrayList = new ArrayList<String>();
//        timeStampArrayList = readDataFromActivityMain.getStringArrayList("timeStampArrayList");

        volumeArrayList = new ArrayList<Double>();
//        volumeArrayList = (ArrayList<Double>) getIntent().getSerializableExtra("volumeArrayList");

        deviceReference = FirebaseDatabase.getInstance().getReference().child("Devices").child("Wasav-001");
        deviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

//                    timeStamp = ds.getKey();
                    timeStampArrayList.add(ds.getKey());

//                    volume = (Double) ds.child("volume").getValue();
                    volumeArrayList.add((Double) ds.child("volume").getValue());

                }

                plusMinusWeeks = 0;
                changeWeek(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        homeRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toActivityMain = new Intent(ActivityStatistic.this, ActivityMain.class);
                startActivity(toActivityMain);
            }
        });

        profileRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toActivityProfile = new Intent(ActivityStatistic.this, ActivityProfile.class);
                startActivity(toActivityProfile);
            }
        });

        minusRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeWeek(-1);
            }
        });

        plusRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeWeek(1);
            }
        });
    }

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

    private String convertMonth (int input){

        String output ="";

        if(input == 1){

            output = "Jan";
        }
        else if(input == 2){

            output = "Feb";
        }
        else if(input == 3){

            output = "Mar";
        }
        else if(input == 4){

            output = "Apr";
        }
        else if(input == 5){

            output = "May";
        }
        else if(input == 6){

            output = "Jun";
        }
        else if(input == 7){

            output = "Jul";
        }
        else if(input == 8){

            output = "Aug";
        }
        else if(input == 9){

            output = "Sep";
        }
        else if(input == 10){

            output = "Oct";
        }
        else if(input == 11){

            output = "Nov";
        }
        else if(input == 12){

            output = "Dec";
        }

        return output;
    }

    private void changeWeek (int input){

        newDate = Calendar.getInstance();
        newDate.setTime(new Date());

        plusMinusWeeks = plusMinusWeeks + input;

        newDate.add(Calendar.WEEK_OF_YEAR, plusMinusWeeks);
        int startDateOfYear = newDate.get(Calendar.DAY_OF_YEAR);
        int startDay = newDate.get(Calendar.DAY_OF_MONTH) + 1 - newDate.get(Calendar.DAY_OF_WEEK);
        int startMonth = newDate.get(Calendar.MONTH) + 1;
        int startYear = newDate.get(Calendar.YEAR);

        newDate.add(Calendar.DAY_OF_YEAR, 7 - newDate.get(Calendar.DAY_OF_WEEK));
        int endDateOfYear = newDate.get(Calendar.DAY_OF_YEAR);
        int endDay = newDate.get(Calendar.DAY_OF_MONTH);
        int endMonth = newDate.get(Calendar.MONTH) + 1;
        int endYear = newDate.get(Calendar.YEAR);

        newDate.add(Calendar.WEEK_OF_YEAR, -1);
        axisValues = new ArrayList();

        if(startDay == 0){

            newDate.add(Calendar.WEEK_OF_YEAR, 0);
            startDay = newDate.get(Calendar.DAY_OF_MONTH) + 1;
            dateTextView.setText(startDay + " " + convertMonth(startMonth - 1) + " " + " - " + endDay + " " + convertMonth(endMonth) + " " );
        }
        else {

            dateTextView.setText(startDay + " " + convertMonth(startMonth) + " " + " - " + endDay + " " + convertMonth(endMonth) + " " );

        }

        newtimeStampArrayList = new ArrayList<String>();
        newVolumeArrayList = new ArrayList<Double>();

        for (int i = 0; i <= 6; i++){

            newDate.add(Calendar.DAY_OF_YEAR, 1);
            int newDay = newDate.get(Calendar.DAY_OF_MONTH);
            int newMonth = newDate.get(Calendar.MONTH) + 1;
            int newYear = newDate.get(Calendar.YEAR);
            axisValues.add(i, new AxisValue(i).setLabel(newDay + " " + convertMonth(newMonth)));

            for(int j = 0; j < timeStampArrayList.size(); j++){

                if(splitterDay(String.valueOf(timeStampArrayList.get(j))).equals(String.valueOf(newDay)) && splitterMonth(String.valueOf(timeStampArrayList.get(j))).equals(String.valueOf(newMonth))
                        && splitterYear(String.valueOf(timeStampArrayList.get(j))).equals(String.valueOf(newYear))){

                    newtimeStampArrayList.add(timeStampArrayList.get(j));
                    newVolumeArrayList.add(volumeArrayList.get(j));
                }
            }
        }

        for(int i = 0; i < newtimeStampArrayList.size(); i++){

            if(i < newtimeStampArrayList.size() - 1 && splitterDay(String.valueOf(newtimeStampArrayList.get(i))).equals(splitterDay(String.valueOf(newtimeStampArrayList.get(i+1))))
                    && splitterMonth(String.valueOf(newtimeStampArrayList.get(i))).equals(splitterMonth(String.valueOf(newtimeStampArrayList.get(i+1))))
                    && splitterYear(String.valueOf(newtimeStampArrayList.get(i))).equals(splitterYear(String.valueOf(newtimeStampArrayList.get(i+1))))){

                newtimeStampArrayList.set(i, newtimeStampArrayList.get(i));
                newtimeStampArrayList.remove(i + 1);
                newVolumeArrayList.set(i, newVolumeArrayList.get(i) + newVolumeArrayList.get(i+1));
                newVolumeArrayList.remove(i + 1);
            }
        }

        Log.d("newVolumeArrayList", newtimeStampArrayList + " " + String.valueOf(newVolumeArrayList));

        yAxisValues = new ArrayList();

        newDate.add(Calendar.DAY_OF_YEAR, -7);

        for (int i = 0; i <= 6; i++) {

            newDate.add(Calendar.DAY_OF_YEAR, 1);
            int newDay = newDate.get(Calendar.DAY_OF_MONTH);
            int newMonth = newDate.get(Calendar.MONTH) + 1;
            int newYear = newDate.get(Calendar.YEAR);

            boolean state = false;

            for(int j = 0; j < newtimeStampArrayList.size(); j++){

                if(splitterDay(newtimeStampArrayList.get(j)).equals(String.valueOf(newDay))
                        && splitterMonth(newtimeStampArrayList.get(j)).equals(String.valueOf(newMonth))
                        && splitterYear(newtimeStampArrayList.get(j)).equals(String.valueOf(newYear))){

                    yAxisValues.add(new PointValue(i, newVolumeArrayList.get(j).floatValue()));
                    state = true;
                }
            }

            if (state == false){

                yAxisValues.add(new PointValue(i, (float) 0.0));
            }
        }

        Double totalUsage = 0.0;
        for(int i = 0; i < newVolumeArrayList.size(); i++){

            totalUsage = totalUsage + newVolumeArrayList.get(i);
        }

        if(totalUsage == 0.0){

            totalUsageTextView.setText(String.valueOf(totalUsage) + " L");
        }
        else {

            totalUsageTextView.setText(new DecimalFormat("#.0#").format(totalUsage) + " L");
        }

        Line line = new Line(yAxisValues).setColor(getResources().getColor(R.color.mediumturqoise));

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(getResources().getColor(R.color.darkgray));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
//        yAxis.setName("Volume in Liters");
        yAxis.setTextColor(getResources().getColor(R.color.darkgray));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
//        viewport.top = 10;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}
