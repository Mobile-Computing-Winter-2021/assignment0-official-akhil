package com.example.helloworld;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    TextView name;
    boolean check1, check2, check3, check4, check5;
    String Name;
    CheckBox c1, c2, c3, c4, c5;
    Button check;
    String msg;
    String change;
    private static final String INFO_TAG= "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.i(INFO_TAG,"Second Activity is Created");
        change = "Second Activity is Created";
        Toast.makeText(getApplicationContext(), change, Toast.LENGTH_SHORT).show();

        name = (TextView)findViewById(R.id.textView6);

        Intent intent = getIntent();

        Name = intent.getStringExtra("Name");
        check1 = intent.getBooleanExtra("check1",false);
        check2 = intent.getBooleanExtra("check2",false);
        check3 = intent.getBooleanExtra("check3",false);
        check4 = intent.getBooleanExtra("check4",false);
        check5 = intent.getBooleanExtra("check5",false);


        name.setText(Name);
        c1 = (CheckBox) findViewById(R.id.checkBox6);
        c2 = (CheckBox) findViewById(R.id.checkBox);
        c3 = (CheckBox) findViewById(R.id.checkBox7);
        c4 = (CheckBox) findViewById(R.id.checkBox8);
        c5 = (CheckBox) findViewById(R.id.checkBox9);


        if(check1==false)
        {
            c1.setAlpha(0.4f);
        }
        else
        {
            c1.setChecked(check1);
            c1.setAlpha(1.0f);
        }
        if(check2==false)
        {
            c2.setAlpha(0.4f);
        }
        else
        {
            c2.setChecked(check2);
            c2.setAlpha(1.0f);
        }
        if(check3==false)
        {
            c3.setAlpha(0.4f);
        }
        else
        {
            c3.setChecked(check3);
            c3.setAlpha(1.0f);
        }
        if(check4==false)
        {
            c4.setAlpha(0.4f);
        }
        else
        {
            c4.setChecked(check4);
            c4.setAlpha(1.0f);
        }
        if(check5==false)
        {
            c5.setAlpha(0.4f);
        }
        else
        {
            c5.setChecked(check5);
            c5.setAlpha(1.0f);
        }


        check = (Button) findViewById(R.id.button2);
        check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(check1 && check2 && check3 && check4 && check5)
                    msg = "You're Safe";
                else
                    msg = "You're Not Safe";

                Toast.makeText(getApplicationContext() , msg, Toast.LENGTH_SHORT).show();
                returnAnswer(msg);
            }
        });

    }
    void returnAnswer(String msg)
    {
        Intent data = new Intent();
        data.putExtra("message",msg);
        setResult(Activity.RESULT_OK,data);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(INFO_TAG,"Second Activity State is RESUMED");
        change = "Second Activity State is RESUMED";
        Toast.makeText(getApplicationContext(), change, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onPause()
    {
        super.onPause();
        Log.i(INFO_TAG,"State of Second Activity changed from RESUMED to PAUSED");
        change = "State of Second Activity changed from RESUMED to PAUSED";
        Toast.makeText(getApplicationContext(), change, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i(INFO_TAG,"Second Activity is Destroyed");
        change = "Second Activity is Destroyed";
        Toast.makeText(getApplicationContext(), change, Toast.LENGTH_SHORT).show();
    }
}
