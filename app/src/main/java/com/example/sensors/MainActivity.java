package com.example.sensors;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener;
    private LocationManager locationManager;
    private LocationListener listener;
    private Sensor mAcc, lAcc, light, prox, temp;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch acc_switch,lacc_switch, light_switch, prox_switch, temp_switch, loc_switch;
    TextView acc_x, acc_y, acc_z, l_acc_x, l_acc_y, l_acc_z, light_val, prox_value, temp_value, lati, longi;
    int flag,flag1,flag2,flag3,flag4,flag5=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        prox = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        temp = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        temp_value = findViewById(R.id.temperature);
        if (temp == null) {
            temp_value.setText("This sensor is not available");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                loc_switch = findViewById(R.id.switch_loc);
                loc_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            locationManager.removeUpdates(listener);
                            lati.setText("Not collecting data");
                            longi.setText("Not collecting data");
                            flag5=1;
                        }
                        else
                        {
                            flag5=0;
                            lati.setText("Not updated yet");
                            longi.setText("Not updated yet");
                            boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if(isGPS)
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                            else
                                Toast.makeText(getApplicationContext(),"Turn on GPS", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if(flag5==0) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    lati = findViewById(R.id.latitude);
                    longi = findViewById(R.id.longitude);
                    String lati_value = String.format(Locale.ENGLISH, "Latitude = %.7f", latitude);
                    String longi_value = String.format(Locale.ENGLISH, "Longitude = %.7f", longitude);
                    lati.setText(lati_value);
                    longi.setText(longi_value);
                }
                else
                {
                    lati.setText("Not updated yet");
                    longi.setText("Not updated yet");
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

        };
        mSensorListener = new SensorEventListener() {
            @Override
            public final void onAccuracyChanged(Sensor sensor, int accuracy) {
                //sensor accuracy changes
            }

            @Override
            public final void onSensorChanged(final SensorEvent event) {
                Sensor sensor = event.sensor;
                // Accelerometer
                acc_switch = findViewById(R.id.switch_acc);
                acc_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            mSensorManager.unregisterListener(mSensorListener, mAcc);
                            acc_x.setText("Sensor off");
                            acc_y.setText("Sensor off");
                            acc_z.setText("Sensor off");
                            flag=1;
                        } else {
                            mSensorManager.registerListener(mSensorListener, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
                            flag=0;
                        }
                    }
                });
                lacc_switch = findViewById(R.id.switch_lacc);
                lacc_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            mSensorManager.unregisterListener(mSensorListener, lAcc);
                            l_acc_x.setText("Sensor off");
                            l_acc_y.setText("Sensor off");
                            l_acc_z.setText("Sensor off");
                            flag1=1;
                        } else {
                            mSensorManager.registerListener(mSensorListener, lAcc, SensorManager.SENSOR_DELAY_NORMAL);
                            flag1=0;
                        }
                    }
                });
                light_switch = findViewById(R.id.switch_light);
                light_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            mSensorManager.unregisterListener(mSensorListener, light);
                            light_val.setText("Sensor off");
                            flag2=1;
                        }
                        else
                        {
                            mSensorManager.registerListener(mSensorListener, light, SensorManager.SENSOR_DELAY_NORMAL);
                            flag2=0;
                        }
                    }
                });
                prox_switch = findViewById(R.id.switch_prox);
                prox_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            mSensorManager.unregisterListener(mSensorListener, prox);
                            prox_value.setText("Sensor off");
                            flag3=1;
                        }
                        else{
                            mSensorManager.registerListener(mSensorListener, prox, SensorManager.SENSOR_DELAY_NORMAL);
                            flag3=0;
                        }
                    }
                });
                temp_switch = findViewById(R.id.switch_temp);
                temp_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            mSensorManager.unregisterListener(mSensorListener, temp);
                            temp_value.setText("Sensor off");
                            flag4=1;
                        }
                        else
                        {
                            temp = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
                            if (temp == null) {
                                temp_value.setText("This sensor is not available");
                                flag4=0;
                            }
                            mSensorManager.registerListener(mSensorListener, temp, SensorManager.SENSOR_DELAY_NORMAL);
                            flag4=0;
                        }
                    }
                });

                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    if(flag==0) {
                        acc_x = findViewById(R.id.xaxis);
                        acc_y = findViewById(R.id.yaxis);
                        acc_z = findViewById(R.id.zaxis);
                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];
                        String acc_x_data = "X=" + x;
                        String acc_y_data = "Y=" + y;
                        String acc_z_data = "Z=" + z;
                        acc_x.setText(acc_x_data);
                        acc_y.setText(acc_y_data);
                        acc_z.setText(acc_z_data);
                    }
                    else
                    {
                        acc_x.setText("Sensor off");
                        acc_y.setText("Sensor off");
                        acc_z.setText("Sensor off");
                    }

                }
                // Linear Acceleration
                else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                    if(flag1==0) {
                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];
                        l_acc_x = findViewById(R.id.l_xaxis);
                        l_acc_y = findViewById(R.id.l_yaxis);
                        l_acc_z = findViewById(R.id.l_zaxis);

                        String lacc_x_data = String.format(Locale.ENGLISH, "X = %.7f", x);
                        String lacc_y_data = String.format(Locale.ENGLISH, "Y = %.7f", y);
                        String lacc_z_data = String.format(Locale.ENGLISH, "Z = %.7f", z);
                        l_acc_x.setText(lacc_x_data);
                        l_acc_y.setText(lacc_y_data);
                        l_acc_z.setText(lacc_z_data);
                    }
                    else
                    {
                        l_acc_x.setText("Sensor off");
                        l_acc_y.setText("Sensor off");
                        l_acc_z.setText("Sensor off");
                    }
                } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
                    if(flag2==0) {
                        float x = event.values[0];
                        light_val = findViewById(R.id.light);
                        String l = "Illuminance = " + x;
                        light_val.setText(l);
                    }
                    else
                    {
                        light_val.setText("Sensor off");
                    }
                } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    if(flag3==0) {
                        float x = event.values[0];
                        prox_value = findViewById(R.id.proximity);
                        String p = "Value = " + x;
                        prox_value.setText(p);
                    }
                    else
                    {
                        prox_value.setText("Sensor off");
                    }
                } else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    if(flag4==0) {
                        float x = event.values[0];
                        String t = "Value = " + x;
                        temp_value.setText(t);
                    }
                    else
                    {
                        temp_value.setText("This sensor is not available");
                    }
                }
            }
        };
        mSensorManager.registerListener(mSensorListener, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mSensorListener, lAcc, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mSensorListener, light, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mSensorListener, prox, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mSensorListener, temp, SensorManager.SENSOR_DELAY_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}
                        , 1);
            }
            return;
        }
        boolean isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPS)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        else
            Toast.makeText(getApplicationContext(),"Turn on GPS", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }
}