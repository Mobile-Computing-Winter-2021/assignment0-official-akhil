package com.example.indoorlocalization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
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
import java.util.Arrays;
import java.util.List;

public class testknn extends AppCompatActivity {
    WifiManager mWifiManager;
    Button checkloc;
    TextView show_result, textv;
    EditText k_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testknn);
        checkloc = findViewById(R.id.button4);
        show_result = findViewById(R.id.textView6);
        show_result.setVisibility(TextView.INVISIBLE);
        textv = findViewById(R.id.textView8);
        textv.setVisibility(TextView.INVISIBLE);
        mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        checkloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mWifiManager.startScan();
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
            BarChart barChart = findViewById(R.id.knnbarchart);
            ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            int count = 0;
            int mineval = 0;
            int repeaterval = 0;
            int hariomval = 0;
            int zero_val = 0;
            int one_val = 0 ;
            double eucl_calc=0;
            ArrayList<Double> eucl_distance = new ArrayList<>();
            ArrayList<Integer> locations = new ArrayList<>();
            int locationval=0;
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                List<ScanResult> wifiList = mWifiManager.getScanResults();
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
                            mineval = wifiList.get(i).level;
                            break;
                        case "Repeater":
                            repeaterval = wifiList.get(i).level;
                            break;
                        case "Hariom":
                            hariomval = wifiList.get(i).level;
                            break;
                    }
                }
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
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "RSSIdata").allowMainThreadQueries().build();
                RSSIDao rssiDao = db.rssiDao();
                List<RSSItable> rssi_values = rssiDao.getRSSIvalues();
                for (RSSItable rssit : rssi_values) {
                    eucl_calc = Math.sqrt(Math.pow(mineval - rssit.getA1(),2) + Math.pow(repeaterval - rssit.getA2(),2) + Math.pow(hariomval - rssit.getA3(),2));
                    eucl_distance.add(eucl_calc);
                }
                Double[] e_dist = new Double[eucl_distance.size()];
                e_dist = eucl_distance.toArray(e_dist);
                Arrays.sort(e_dist);
                k_value = findViewById(R.id.kvalue);
                int k = Integer.parseInt(k_value.getText().toString());
                for (RSSItable rssit : rssi_values) {
                    if(locations.size()==k)
                    {
                        break;
                    }
                    eucl_calc = Math.sqrt(Math.pow(mineval - rssit.getA1(),2) + Math.pow(repeaterval - rssit.getA2(),2) + Math.pow(hariomval - rssit.getA3(),2));
                    for(int i=0; i<k;i++)
                    {
                        if(eucl_calc == e_dist[i])
                        {
                            locations.add(rssit.getLoc());
                            break;
                        }
                    }
                }

                boolean[] visited = new boolean[locations.size()];

                Arrays.fill(visited, false);
                int loc_count=0;
                for (int i = 0; i < locations.size(); i++) {
                    if (visited[i])
                        continue;
                    int count1 = 1;
                    for (int j = i + 1; j < locations.size(); j++) {
                        if (locations.get(i).equals(locations.get(j))) {
                            visited[j] = true;
                            count1++;
                        }
                    }
                    if (count1 > loc_count) {
                        loc_count = count1;
                        locationval = locations.get(i);
                    }
                }
                show_result.setText(" Your Location is.. Room "+ String.valueOf(locationval));
                show_result.setVisibility(TextView.VISIBLE);
                textv.setText("Number of times this Room was classified.. "+String.valueOf(loc_count));
                textv.setVisibility(TextView.VISIBLE);
            }
        }
    };
}