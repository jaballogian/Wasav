package com.example.wasav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class ActivityStatistic extends AppCompatActivity {

    private RelativeLayout homeRelativeLayout, profileRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        homeRelativeLayout = (RelativeLayout) findViewById(R.id.homeRelativeLayoutActivityStatistic);
        profileRelativeLayout = (RelativeLayout) findViewById(R.id.profileRelativeLayoutActivityStatistic);

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
}
