package com.mok.budget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main_scn extends AppCompatActivity {
    private String id, second_id;
    fragment_home fragmentHome;
    fragment_calendar fragmentCalendar;
    fragment_joint fragmentJoint;
    fragment_setting fragmentSetting;
    NavigationBarView bottom_navi;
    Bundle bundle;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scn);

        fragmentHome = new fragment_home();
        fragmentCalendar = new fragment_calendar();
        fragmentJoint = new fragment_joint();
        fragmentSetting = new fragment_setting();
        bundle = new Bundle();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = sdf.format(now);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        second_id = intent.getStringExtra("second_id");

        bundle.putString("id", id);
        bundle.putString("second_id", second_id);

        fragmentHome.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, fragmentHome).commit();

        bottom_navi = findViewById(R.id.bottom_navi);

        menu = bottom_navi.getMenu();

        if (second_id != null){
            MenuItem jointItem = menu.findItem(R.id.navigation_joint);
            jointItem.setEnabled(false);
            jointItem.setVisible(false);
            MenuItem settingItem = menu.findItem(R.id.navigation_setting);
            settingItem.setEnabled(false);
            settingItem.setVisible(false);
        }

        bottom_navi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.navigation_home:
                        fragmentHome.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, fragmentHome).commit();
                        return true;
                    case R.id.navigation_calendar:
                        fragmentCalendar.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, fragmentCalendar).commit();
                        return true;
                    case R.id.navigation_joint:
                        fragmentJoint.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, fragmentJoint).commit();
                        return true;
                    case R.id.navigation_setting:
                        fragmentSetting.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, fragmentSetting).commit();
                        return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed(){
        if (second_id == null){
            Intent intent = new Intent(Main_scn.this, Login.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }
}