package com.example.indoorlocalization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {RSSItable.class}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract RSSIDao rssiDao();
}

@Entity
class RSSItable
{
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "Location")
    public int loc;

    @ColumnInfo(name = "A1")
    public int a1;

    @ColumnInfo(name = "A2")
    public int a2;

    @ColumnInfo(name = "A3")
    public int a3;

    public int getLoc() {
        return loc;
    }
    public void setLoc(int loc_val) {
        this.loc = loc_val;
    }
    public int getA1() {
        return a1;
    }
    public void setA1(int a1_val)
    {
        this.a1 = a1_val;
    }
    public int getA2() {
        return a2;
    }
    public void setA2(int a2_val) {
        this.a2 = a2_val;
    }
    public int getA3() {
        return a3;
    }
    public void setA3(int a3_val) {
        this.a3 = a3_val;
    }
}

@Dao
interface RSSIDao
{
    @Insert
    void addRSSI(RSSItable value);
    @Query("select * from RSSItable")
    public List<RSSItable> getRSSIvalues();
}

public class WarDrive extends AppCompatActivity {

    WifiManager mWifiManager;
    Button samples;
    TextView sampletext, status;
    EditText roomnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_war_drive);
        samples = findViewById(R.id.sample);
        sampletext = findViewById(R.id.textView3);
        status = findViewById(R.id.textView4);
        status.setVisibility(TextView.INVISIBLE);
        sampletext.setVisibility(TextView.INVISIBLE);
        mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        samples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mWifiManager.startScan();
                sampletext.setTextColor(Color.BLACK);
                sampletext.setText("Searching for RSSI values");
                status.setVisibility(TextView.VISIBLE);
                sampletext.setVisibility(TextView.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        try{
            if(mWifiScanReceiver!=null)
            {
                unregisterReceiver(mWifiScanReceiver);
            }
        }catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }
    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context c, Intent intent)
        {
            BarChart barChart = findViewById(R.id.wdbarchart);
            ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "RSSIdata").allowMainThreadQueries().build();
            RSSIDao rssiDao = db.rssiDao();
            roomnumber = findViewById(R.id.editText);
            int rnumber = Integer.parseInt(roomnumber.getText().toString());
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                RSSItable rssi_val = new RSSItable();
                rssi_val.setLoc(rnumber);
                List<ScanResult> wifiList = mWifiManager.getScanResults();
                int mine_val = 0;
                int repeater_val = 0;
                int hariom_val = 0;
                int count = 0;
                for (int i = 0; i < wifiList.size(); i++)
                {
                    if(!(wifiList.get(i).SSID.equals("Mine") || wifiList.get(i).SSID.equals("Repeater") || wifiList.get(i).SSID.equals("Hariom")))
                    {
                        continue;
                    }
                    barEntryArrayList.add(new BarEntry(count, 100+wifiList.get(i).level));
                    labels.add(wifiList.get(i).SSID);
                    count++;
                    switch (wifiList.get(i).SSID) {
                        case "Mine":
                            mine_val = wifiList.get(i).level;
                            break;
                        case "Repeater":
                            repeater_val = wifiList.get(i).level;
                            break;
                        case "Hariom":
                            hariom_val = wifiList.get(i).level;
                            break;
                    }
                }
                rssi_val.setA1(mine_val);
                rssi_val.setA2(repeater_val);
                rssi_val.setA3(hariom_val);
                rssiDao.addRSSI(rssi_val);
                BarDataSet bardataset = new BarDataSet(barEntryArrayList, "");
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                bardataset.setValueTextSize(10f);

                BarData data = new BarData(bardataset);
                barChart.setData(data);
                data.setDrawValues(false);
                barChart.getAxisRight().setEnabled(false);

                barChart.getXAxis().setGranularity(1f);
                barChart.getXAxis().setGranularityEnabled(true);
                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                barChart.getDescription().setEnabled(false);
                barChart.getLegend().setEnabled(false);

                barChart.getAxisLeft().setValueFormatter(new ValueFormatter()
                {
                    @Override
                    public String getFormattedValue(float value)
                    {
                        return String.valueOf((int) value - 100) +"dBm";
                    }
                });

                barChart.getAxisLeft().setAxisMinimum(0);
                barChart.animateY(2000);
                sampletext = findViewById(R.id.textView3);
                status = findViewById(R.id.textView4);
                sampletext.setTextColor(Color.MAGENTA);
                sampletext.setText("Sample Collected");
                List<RSSItable> rssi_values =rssiDao.getRSSIvalues();
                for(RSSItable rssit : rssi_values) {
                    Log.i("Values", String.valueOf(rssit.getLoc()));
                    Log.i("Values", String.valueOf(rssit.getA1()));
                    Log.i("Values", String.valueOf(rssit.getA2()));
                    Log.i("Values", String.valueOf(rssit.getA3()));
                }
            }
        }
    };
}