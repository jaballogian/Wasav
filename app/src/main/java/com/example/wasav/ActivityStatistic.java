package com.example.wasav;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ActivityStatistic extends AppCompatActivity {

    private RelativeLayout homeRelativeLayout, profileRelativeLayout, leftRelativeLayout, rightRelativeLayout;
    private TextView dateTextView, totalUsageTextView;
    private LineChartView lineChartView;
    private Bundle readDataFromActivityMain;
    private ArrayList<String> timeStampArrayList;
    private ArrayList yAxisValues, axisValues;
    private String[] splitterMinus, splitterSlash;
    private ArrayList<Double> volumeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        homeRelativeLayout = (RelativeLayout) findViewById(R.id.homeRelativeLayoutActivityStatistic);
        profileRelativeLayout = (RelativeLayout) findViewById(R.id.profileRelativeLayoutActivityStatistic);
        leftRelativeLayout = (RelativeLayout) findViewById(R.id.leftRelativeLayoutActivityStatistic);
        rightRelativeLayout = (RelativeLayout) findViewById(R.id.rightRelativeLayoutActivityStatistic);
        dateTextView = (TextView) findViewById(R.id.dateTextViewActivityStatistic);
        lineChartView = (LineChartView) findViewById(R.id.chartActvityStatistic);
        totalUsageTextView = (TextView) findViewById(R.id.totalUsageTextViewActivityStatistic);
        readDataFromActivityMain = getIntent().getExtras();

        timeStampArrayList = new ArrayList<String>();
        timeStampArrayList = readDataFromActivityMain.getStringArrayList("timeStampArrayList");

        yAxisValues = new ArrayList();
        axisValues = new ArrayList();

        volumeArrayList = new ArrayList<Double>();
        volumeArrayList = (ArrayList<Double>) getIntent().getSerializableExtra("volumeArrayList");

        for (int i = 0; i < timeStampArrayList.size(); i++) {
            axisValues.add(i, new AxisValue(i).setLabel(splitterDay(timeStampArrayList.get(i)) + " " + convertMonth(Integer.parseInt(splitterMonth(timeStampArrayList.get(i))))));
        }

        for (int i = 0; i < volumeArrayList.size(); i++) {
            yAxisValues.add(new PointValue(i, volumeArrayList.get(i).floatValue()));
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
}
