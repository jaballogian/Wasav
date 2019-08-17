package com.example.wasav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

import lecho.lib.hellocharts.model.Line;

public class ActivityProfile extends AppCompatActivity {

    private LinearLayout logoutLinearLayout;
    private RelativeLayout homeRelativeLayout, statisticRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutLinearLayout = (LinearLayout) findViewById(R.id.logoutLinearLayoutActivityProfile);
        homeRelativeLayout = (RelativeLayout) findViewById(R.id.homeRelativeLayoutActivityProfile);
        statisticRelativeLayout = (RelativeLayout) findViewById(R.id.statisticRelativeLayoutActivityProfile);

        logoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent toActivityLogin = new Intent(ActivityProfile.this, ActivityLogin.class);
                toActivityLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toActivityLogin);
                finish();
            }
        });

        homeRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toActivityMain = new Intent(ActivityProfile.this, ActivityMain.class);
                startActivity(toActivityMain);
            }
        });

        statisticRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toActivityStatistic = new Intent(ActivityProfile.this, ActivityStatistic.class);
                startActivity(toActivityStatistic);
            }
        });
    }
}
