package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = new Main_Fragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

    }
    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(power);
        unregisterReceiver(batokay);
    }

    PowerReceiver power;
    BOkay batokay;
    @Override
    protected void onResume()
    {
        super.onResume();
        batokay = new BOkay();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(Intent.ACTION_BATTERY_OKAY);
        intentFilter1.addAction(Intent.ACTION_BATTERY_LOW);
        registerReceiver(batokay, intentFilter1);
        power = new PowerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(power, intentFilter);

    }
}