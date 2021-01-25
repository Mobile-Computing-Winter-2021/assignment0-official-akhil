package com.example.helloworld;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    CheckBox ch1, ch2, ch3, ch4, ch5;
    Button submit_button;
    EditText send_text;
    private int requestcode=0;
    String change;
    private static final String INFO_TAG= "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(INFO_TAG,"Main Activity is Created");
        change = "Main Activity is Created";
        Toast.makeText(getApplicationContext(), change, Toast.LENGTH_SHORT).show();
        submit_button = (Button)findViewById(R.id.button3);
        send_text = (EditText)findViewById(R.id.Name);
        ch1=(CheckBox)findViewById(R.id.checkBox1);
        ch2=(CheckBox)findViewById(R.id.checkBox2);
        ch3=(CheckBox)findViewById(R.id.checkBox3);
        ch4=(CheckBox)findViewById(R.id.checkBox4);
        ch5=(CheckBox)findViewById(R.id.checkBox5);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = send_text.getText().toString();
                if (str.matches("")) {
                    str = "\t\t\t\t\t\t\t\t\t\t\t\tName = Not Entered";
                }
                else {
                    str = "\t\t\t\t\t\t\t\t\t\t\t\tName = " + str;
                }
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    intent.putExtra("Name", str);
                    boolean check1 = ch1.isChecked();
                    intent.putExtra("check1", check1);
                    boolean check2 = ch2.isChecked();
                    intent.putExtra("check2", check2);
                    boolean check3 = ch3.isChecked();
                    intent.putExtra("check3", check3);
                    boolean check4 = ch4.isChecked();
                    intent.putExtra("check4", check4);
                    boolean check5 = ch5.isChecked();
                    intent.putExtra("check5", check5);
//                    Log.i(INFO_TAG,"State of Main Activity changed from RESUMED to PAUSED");
//                    change = "State of Main Activity changed from RESUMED to PAUSED";
//                    Toast.makeText(getApplicationContext(), change, Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, requestcode);
                }
        });
    }
    public void clear(View v) {
        if (ch1.isChecked()) {
            ch1.setChecked(false);
        }
        if (ch2.isChecked()) {
            ch2.setChecked(false);
        }
        if (ch3.isChecked()) {
            ch3.setChecked(false);
        }
        if (ch4.isChecked()) {
            ch4.setChecked(false);
        }
        if (ch5.isChecked()) {
            ch5.setChecked(false);
        }
        EditText et=(EditText) findViewById(R.id.Name);
        et.setText("");
    }
    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(INFO_TAG,"Main Activity State is RESUMED");
        change = "Main Activity State is RESUMED";
        Toast.makeText(getApplicationContext(), change, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onPause()
    {
        super.onPause();
        Log.i(INFO_TAG,"State of Main Activity changed from RESUMED to PAUSED");
        change = "State of Main Activity changed from RESUMED to PAUSED";
        Toast.makeText(getApplicationContext(), change, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i(INFO_TAG,"Main Activity is Destroyed");
        change = "Main Activity is Destroyed";
        Toast.makeText(getApplicationContext(), change, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        String msg;
        if (requestCode == requestcode && resultCode == RESULT_OK && data != null) {
            msg = data.getStringExtra("message");
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}