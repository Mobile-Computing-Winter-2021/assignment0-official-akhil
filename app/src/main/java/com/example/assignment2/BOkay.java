package com.example.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BOkay extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if(action.equals(Intent.ACTION_BATTERY_LOW))
            Toast.makeText(context,"Battery is Low", Toast.LENGTH_LONG).show();
        else if(action.equals(Intent.ACTION_BATTERY_OKAY))
            Toast.makeText(context,"Battery is Okay", Toast.LENGTH_LONG).show();
    }
}
