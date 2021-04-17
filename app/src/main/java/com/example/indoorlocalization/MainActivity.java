package com.example.indoorlocalization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class MainActivity extends AppCompatActivity {
    WifiManager mWifiManager;
    Button wardrive, test, test_knn, imu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}
                        , 1);
            }
            return;
        }
        setContentView(R.layout.activity_main);
        wardrive = findViewById(R.id.wardriving);
        test = findViewById(R.id.button3);
        test_knn = findViewById(R.id.button2);
        imu = findViewById(R.id.button5);
        wardrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WarDrive.class);
                startActivity(intent);
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestRSSI.class);
                startActivity(intent);
            }
        });
        test_knn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), testknn.class);
                startActivity(intent);
            }
        });
        imu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IMU.class);
                startActivity(intent);
            }
        });
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mWifiManager.startScan();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mWifiScanReceiver);
    }

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            BarChart barChart = findViewById(R.id.barchart);
            ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            int count = 0;
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> wifidetails = mWifiManager.getScanResults();
                for (int i = 0; i < wifidetails.size(); i++) {
                    if (wifidetails.get(i).SSID.equals("")) {
                        continue;
                    }
                    barEntryArrayList.add(new BarEntry(count,100+wifidetails.get(i).level));
                    String lab = (wifidetails.get(i).SSID).substring(0,Math.min(8, wifidetails.get(i).SSID.length()));
                    labels.add(lab);
                    count++;
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
                        return ((int) value - 100) +"dBm";
                    }
                });

                barChart.getAxisLeft().setAxisMinimum(0);
                barChart.animateY(2000);
            }
        }
    };

}
