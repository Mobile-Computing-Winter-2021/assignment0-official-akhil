package com.example.sensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
@Database(entities = {Accelerometer_data.class, Light.class}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract SensorDao senserDao();
}

@Entity
class Accelerometer_data {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "x-axis")
    public float x;

    @ColumnInfo(name = "y-axis")
    public float y;

    @ColumnInfo(name = "z-axis")
    public float z;

    @ColumnInfo(name = "date")
    public String d;

    public float getX()
    {
        return x;
    }
    public void setX(float x_val)
    {
        this.x = x_val;
    }
    public float getY()
    {
        return y;
    }
    public void setY(float y_val)
    {
        this.y = y_val;
    }
    public float getZ()
    {
        return z;
    }
    public void setZ(float z_val)
    {
        this.z = z_val;
    }
    public String getd()
    {
        return d;
    }
    public void setd(String da)
    {
        this.d = da;
    }
}

@Entity
class Light{
    @PrimaryKey(autoGenerate = true)
    public int lid;

    @ColumnInfo(name = "ilum")
    public float ilum;

    @ColumnInfo(name = "ldate")
    public String light_date;

    public float getilum()
    {
        return ilum;
    }
    public void setilum(float ilum_val)
    {
        this.ilum = ilum_val;
    }
    public String getLight_date()
    {
        return light_date;
    }
    public void setLight_date(String ld)
    {
        this.light_date = ld;
    }

}

@Dao
interface SensorDao {
    @Insert
   void addAcc(Accelerometer_data value);
    @Query("select * from Accelerometer_data WHERE date >= DATE('now', '-1 hour')")
    public List<Accelerometer_data> getaccValues();

    @Insert
    void addLight(Light l_val);
    @Query("select * from Light WHERE ldate >= DATE('now', '-1 hour')")
    public List<Light> getlightValues();
}


public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener;
    private LocationManager locationManager;
    private LocationListener listener;
    private Sensor mAcc, lAcc, light, prox, temp;
    private float acceleration;
    private float cur_acc;
    private float last_acc;
    private int sensed = 0;
    private double sensed_sum = 0;
    private double sensed_avg = 0;

    private final int samp = 50;
    private final double th = 0.3;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch acc_switch,lacc_switch, light_switch, prox_switch, temp_switch, loc_switch;
    TextView acc_x, acc_y, acc_z, l_acc_x, l_acc_y, l_acc_z, light_val, prox_value, temp_value, lati, longi, show_acc,show_light, check_walk;
    int flag,flag1,flag2,flag3,flag4,flag5=0;
    float total_x,total_y,total_z,avg_x,avg_y,avg_z, total_l, avg_l;
    Button b,l;
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
        acceleration = 0.00f;
        cur_acc = SensorManager.GRAVITY_EARTH;
        last_acc = SensorManager.GRAVITY_EARTH;
        b=findViewById(R.id.button);
        l = findViewById(R.id.light_button);
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

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "Sensorsdata").allowMainThreadQueries().build();
        SensorDao senserDao = db.senserDao();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_acc = findViewById(R.id.textView3);
                total_x = 0;
                total_y = 0;
                total_z = 0;
                avg_x = 0;
                avg_y = 0;
                avg_z = 0;

                List<Accelerometer_data> acc_values = senserDao.getaccValues();
//                Toast.makeText(MainActivity.this,"Total values recorded : "+ acc_values.size(), Toast.LENGTH_SHORT).show();
                for (Accelerometer_data accel : acc_values) {
                        float x = accel.getX();
                        float y = accel.getY();
                        float z = accel.getZ();
                        total_x = total_x + x;
                        total_y = total_y + y;
                        total_z = total_z + z;
//                    }
                    avg_x = total_x / acc_values.size();
                    avg_y = total_y / acc_values.size();
                    avg_z = total_z / acc_values.size();
                    String avg = "Total values recorded : " + acc_values.size() + "\nAverage X = " + avg_x + "\nAverage Y = " + avg_y + "\nAverage Z = " + avg_z + "\n\n";
                    show_acc.setText(avg);
                    show_acc.setVisibility(View.VISIBLE);
                }
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_light = findViewById(R.id.textView4);
                total_l=0;
                avg_l=0;
                List<Light> light_values = senserDao.getlightValues();
                    for(Light lig:light_values)
                {
                    float x = lig.getilum();
                    total_l = total_l + x;
                }
                avg_l = total_l/light_values.size();
                String avg_li = "Total values recorded : " + light_values.size() + "\nAverage Light = " + avg_l + "\n\n";
                show_light.setText(avg_li);
                show_light.setVisibility(View.VISIBLE);

            }
        });
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
                            check_walk = findViewById(R.id.textView5);
                            check_walk.setText("Accelerometer Off");
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
                        check_walk = findViewById(R.id.textView5);
                        acc_x = findViewById(R.id.xaxis);
                        acc_y = findViewById(R.id.yaxis);
                        acc_z = findViewById(R.id.zaxis);
                        float x = event.values[0];
                        float y = event.values[1];
                        float z = event.values[2];
                        last_acc = cur_acc;
                        cur_acc = (float)Math.sqrt(x*x + y*y + z*z);
                        float change = cur_acc - last_acc;
                        acceleration = acceleration * 0.9f + change;
                        if (sensed <= samp) {
                            sensed++;
                            sensed_sum += Math.abs(acceleration);
                        } else {
                            sensed_avg = sensed_sum / samp;
                            Log.d("dist", String.valueOf(sensed_avg));
                            if (sensed_avg > th) {
                                check_walk.setText("Walking");
                            } else {
                                check_walk.setText("Stationary");
                            }

                            sensed = 0;
                            sensed_sum = 0;
                            sensed_avg = 0;
                        }
                        Date acc_cal = Calendar.getInstance().getTime();
                        SimpleDateFormat acc_date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
                        String requiredDateformat = acc_date.format(acc_cal);
                        String acc_x_data = "X=" + x;
                        String acc_y_data = "Y=" + y;
                        String acc_z_data = "Z=" + z;
                        acc_x.setText(acc_x_data);
                        acc_y.setText(acc_y_data);
                        acc_z.setText(acc_z_data);
                        Accelerometer_data acc_val = new Accelerometer_data();
                        acc_val.setX(x);
                        acc_val.setY(y);
                        acc_val.setZ(z);
                        acc_val.setd(requiredDateformat);
                        senserDao.addAcc(acc_val);
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
                        Date cal = Calendar.getInstance().getTime();
                        SimpleDateFormat l_date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
                        String l_requiredDateformat = l_date.format(cal);
                        float x = event.values[0];
                        light_val = findViewById(R.id.light);
                        String l = "Illuminance = " + x;
                        light_val.setText(l);
                        Light light_val = new Light();
                        light_val.setilum(x);
                        light_val.setLight_date(l_requiredDateformat);
                        senserDao.addLight(light_val);
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