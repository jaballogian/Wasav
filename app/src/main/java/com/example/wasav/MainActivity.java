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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity {

    private TextView oneDayUsageTextView, sevenDayUsageTextView;
    private DatabaseReference deviceReference;
    private Double oneDayValue, sevenDayUsage, volume, lastSevenDaysVolume;
    private String timeStamp, dayFromApp, monthFromApp, yearFromApp;
    private ArrayList<String> timeStampArrayList;
    private ArrayList<Double> oneDayUsageArrayList, volumeArrayList;
    private String[] splitterMinus, splitterSlash;
    private int dayOfYearFromApp, dayOfYearLast7Days;
//    private ArrayList<Calendar> convertedDateArrayList;

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
//        convertedDateArrayList = new ArrayList<Calendar>();

        //set font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "PRODUCT_SANS.ttf", true);

        //get today date
        Calendar now = Calendar.getInstance();
        dayOfYearFromApp = now.get(Calendar.DAY_OF_YEAR);
        dayFromApp = String.valueOf(now.get(Calendar.DATE));
        monthFromApp = String.valueOf(now.get(Calendar.MONTH) + 1);
        yearFromApp = String.valueOf(now.get(Calendar.YEAR));

        //get last 7 days
        Calendar last7Days = Calendar.getInstance();
        last7Days.setTime(new Date());
        last7Days.add(Calendar.DAY_OF_YEAR, -7);
        dayOfYearLast7Days = last7Days.get(Calendar.DAY_OF_YEAR);
        String sevenDaysBeforeDate1 = String.valueOf(last7Days.get(Calendar.DATE));
        String sevenDaysBeforeDate2 = String.valueOf(last7Days.get(Calendar.MONTH) + 1);
        String sevenDaysBeforeDate3 = String.valueOf(last7Days.get(Calendar.YEAR));

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
}
