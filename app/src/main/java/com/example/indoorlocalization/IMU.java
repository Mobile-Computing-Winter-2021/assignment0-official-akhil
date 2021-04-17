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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

@Database(entities = {Acc_data.class, Gyro_data.class, Magno_data.class}, version = 1)
abstract class AppDatabase1 extends RoomDatabase {
    public abstract SensorDao senserDao();
}

@Entity
class Acc_data {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "Loc")
    public int loc;

    @ColumnInfo(name = "x")
    public float x;

    @ColumnInfo(name = "y")
    public float y;

    @ColumnInfo(name = "z")
    public float z;

    public float getLoc() { return loc; }
    public void setLoc(int loc_val) { this.loc = loc_val; }
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
}

@Entity
class Gyro_data {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "Loc")
    public int loc;

    @ColumnInfo(name = "x")
    public float x;

    @ColumnInfo(name = "y")
    public float y;

    @ColumnInfo(name = "z")
    public float z;

    public float getLoc() { return loc; }
    public void setLoc(int loc_val) { this.loc = loc_val; }
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
}

@Entity
class Magno_data {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "Loc")
    public int loc;

    @ColumnInfo(name = "x")
    public float x;

    @ColumnInfo(name = "y")
    public float y;

    @ColumnInfo(name = "z")
    public float z;

    public float getLoc() { return loc; }
    public void setLoc(int loc_val) { this.loc = loc_val; }
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
}

@Dao
interface SensorDao {
    @Insert
    void addAcc(Acc_data value);
    @Query("select * from Acc_data")
    public List<Acc_data> getaccValues();
    @Insert
    void addGyro(Gyro_data value);
    @Query("select * from Gyro_data")
    public List<Gyro_data> getgyroValues();
    @Insert
    void addMagno(Magno_data value);
    @Query("select * from Magno_data")
    public List<Magno_data> getmagnoValues();
}

public class IMU extends AppCompatActivity {
    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener, mSensorListener1;
    private Sensor mAcc, mgyro, magno;
    private float acc_val, gyro_val, magno_val;
    private float x_acc=0,y_acc=0,z_acc=0,x_gyro=0,y_gyro=0,z_gyro=0,x_magno=0,y_magno=0,z_magno=0, acc_test=0, gyro_test=0, magno_test=0;
    double eucl_distance;
    double maxdistance = 999999;
    int locationval=0;
    EditText imu_final;
    TextView imu_final_text, xt_acc,yt_acc,zt_acc,xt_gyro,yt_gyro,zt_gyro,xt_magno,yt_magno,zt_magno;
    Button start,stop,result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imu);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        result = findViewById(R.id.result);
        imu_final_text = findViewById(R.id.imufinal);
        imu_final_text.setVisibility(TextView.INVISIBLE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mgyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magno = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.registerListener(mSensorListener, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(mSensorListener, mgyro, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(mSensorListener, magno, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.unregisterListener(mSensorListener);
                xt_acc = findViewById(R.id.textView19);
                yt_acc = findViewById(R.id.textView20);
                zt_acc = findViewById(R.id.textView21);
                xt_acc.setText("Sensor Off");
                yt_acc.setText("Sensor Off");
                zt_acc.setText("Sensor Off");
                xt_gyro = findViewById(R.id.textView22);
                yt_gyro = findViewById(R.id.textView23);
                zt_gyro = findViewById(R.id.textView24);
                xt_gyro.setText("Sensor Off");
                yt_gyro.setText("Sensor Off");
                zt_gyro.setText("Sensor Off");
                xt_magno = findViewById(R.id.textView25);
                yt_magno = findViewById(R.id.textView26);
                zt_magno = findViewById(R.id.textView27);
                xt_magno.setText("Sensor Off");
                yt_magno.setText("Sensor Off");
                zt_magno.setText("Sensor Off");
            }
        });
        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorManager.registerListener(mSensorListener1, mAcc, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(mSensorListener1, mgyro, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(mSensorListener1, magno, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        AppDatabase1 db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase1.class, "Sensorsdata").allowMainThreadQueries().build();
        SensorDao senserDao = db.senserDao();
    mSensorListener = new SensorEventListener() {
        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
            //sensor accuracy changes
        }

        @Override
        public final void onSensorChanged(final SensorEvent event) {
            Sensor sensor = event.sensor;
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                imu_final = findViewById(R.id.imuroom);
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                xt_acc = findViewById(R.id.textView19);
                yt_acc = findViewById(R.id.textView20);
                zt_acc = findViewById(R.id.textView21);
                xt_acc.setText(String.valueOf(x));
                yt_acc.setText(String.valueOf(y));
                zt_acc.setText(String.valueOf(z));
                acc_val = (float) Math.sqrt(x * x + y * y + z * z);
                Acc_data acc_value = new Acc_data();
                acc_value.setX(x);
                acc_value.setY(y);
                acc_value.setZ(z);
                acc_value.setLoc(Integer.parseInt(imu_final.getText().toString()));
                senserDao.addAcc(acc_value);

            } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                imu_final = findViewById(R.id.imuroom);
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                xt_gyro = findViewById(R.id.textView22);
                yt_gyro = findViewById(R.id.textView23);
                zt_gyro = findViewById(R.id.textView24);
                xt_gyro.setText(String.valueOf(x));
                yt_gyro.setText(String.valueOf(y));
                zt_gyro.setText(String.valueOf(z));
                gyro_val = (float) Math.sqrt(x * x + y * y + z * z);
                Gyro_data gyro_value = new Gyro_data();
                gyro_value.setX(x);
                gyro_value.setY(y);
                gyro_value.setZ(z);
                gyro_value.setLoc(Integer.parseInt(imu_final.getText().toString()));
                senserDao.addGyro(gyro_value);
            } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                imu_final = findViewById(R.id.imuroom);
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                xt_magno = findViewById(R.id.textView25);
                yt_magno = findViewById(R.id.textView26);
                zt_magno = findViewById(R.id.textView27);
                xt_magno.setText(String.valueOf(x));
                yt_magno.setText(String.valueOf(y));
                zt_magno.setText(String.valueOf(z));
                magno_val = (float) Math.sqrt(x * x + y * y + z * z);
                Magno_data magno_value = new Magno_data();
                magno_value.setX(x);
                magno_value.setY(y);
                magno_value.setZ(z);
                magno_value.setLoc(Integer.parseInt(imu_final.getText().toString()));
                senserDao.addMagno(magno_value);
            }
        }
    };
        mSensorListener1 = new SensorEventListener() {
            @Override
            public final void onAccuracyChanged(Sensor sensor, int accuracy) {
                //sensor accuracy changes
            }

            @Override
            public final void onSensorChanged(final SensorEvent event) {
                if(x_acc !=0 && y_acc !=0 && z_acc!=0 && x_gyro !=0 && y_gyro !=0 && z_gyro!=0 && x_magno !=0 && y_magno !=0 && z_magno!=0)
                {
                    mSensorManager.unregisterListener(mSensorListener1);
                    calc_location();
                }
                Sensor sensor = event.sensor;
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    x_acc = event.values[0];
                    y_acc = event.values[1];
                    z_acc = event.values[2];

                } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    x_gyro = event.values[0];
                    y_gyro = event.values[1];
                    z_gyro = event.values[2];

                } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    x_magno = event.values[0];
                    y_magno = event.values[1];
                    z_magno = event.values[2];
                }
            }
        };
    }
    public void calc_location()
    {
        AppDatabase1 db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase1.class, "Sensorsdata").allowMainThreadQueries().build();
        SensorDao senserDao = db.senserDao();
        List<Acc_data> acc_vals = senserDao.getaccValues();
        List<Gyro_data> gyro_vals = senserDao.getgyroValues();
        List<Magno_data> magno_vals = senserDao.getmagnoValues();
        int k =100;
        float[] acc_final = new float[k];
        float[] gyro_final = new float[k];
        float[] magno_final = new float[k];
        int i=0;
        int j=0;
        int count1=0;
        int count2=0;

        for (Gyro_data gyrol : gyro_vals)
        {
            if(i>=k/2) {
                break;
            }
            if(gyrol.getLoc()==1)
            {
                gyro_final[i]=(float) Math.sqrt(gyrol.getX() * gyrol.getX() + gyrol.getY() * gyrol.getY()+ gyrol.getZ() * gyrol.getZ());
                i++;
                count1++;
            }
        }
        for (Gyro_data gyrol : gyro_vals)
        {
            if(j>=k/2)
            {
                break;
            }

            if(gyrol.getLoc()==2)
            {
                gyro_final[i]=(float) Math.sqrt(gyrol.getX() * gyrol.getX() + gyrol.getY() * gyrol.getY()+ gyrol.getZ() * gyrol.getZ());
                i++;
                j++;
            }
        }


        i=0;
        j=0;
        for (Magno_data manol : magno_vals)
        {
            if(i>=k/2)
                break;
            if(manol.getLoc()==1)
            {
                magno_final[i]=(float) Math.sqrt(manol.getX() * manol.getX() + manol.getY() * manol.getY()+ manol.getZ() * manol.getZ());
                i++;
            }
        }
        for (Magno_data manol : magno_vals)
        {
            if(j>=k/2)
                break;
            if(manol.getLoc()==2)
            {
                magno_final[i]=(float) Math.sqrt(manol.getX() * manol.getX() + manol.getY() * manol.getY()+ manol.getZ() * manol.getZ());
                i++;
                j++;
            }
        }
        i=0;
        j=0;
        for (Acc_data accel : acc_vals) {
            if(i>=k/2)
                break;
            if(accel.getLoc()==1)
            {
                acc_final[i]=(float) Math.sqrt(accel.getX() * accel.getX() + accel.getY() * accel.getY()+ accel.getZ() * accel.getZ());
                i++;
            }
        }
        for (Acc_data accel : acc_vals) {
            if(j>=k/2)
            {
                break;
            }
            if(accel.getLoc()==2 )
            {
                acc_final[i]=(float) Math.sqrt(accel.getX() * accel.getX() + accel.getY() * accel.getY()+ accel.getZ() * accel.getZ());
                i++;
                j++;
            }
        }
        i=0;
        acc_test = (float) Math.sqrt(x_acc * x_acc + y_acc * y_acc+ z_acc * z_acc);
        gyro_test = (float) Math.sqrt(x_gyro * x_gyro + y_gyro * y_gyro+ z_gyro * z_gyro);
        magno_test = (float) Math.sqrt(x_magno * x_magno + y_magno * y_magno + z_magno * z_magno);
        for(i=0;i<gyro_final.length;i++)
        {
            eucl_distance = Math.sqrt(Math.pow(acc_final[i] - acc_test,2) + Math.pow(gyro_final[i] - gyro_test,2) + Math.pow(magno_final[i] - magno_test,2));
            if(eucl_distance<maxdistance)
            {
                maxdistance = eucl_distance;
                if(i<count1)
                    locationval = 1;
                else
                    locationval = 2;
            }
        }
        imu_final_text = findViewById(R.id.imufinal);
        imu_final_text.setText(" Your Location is.. Room "+String.valueOf(locationval));
        imu_final_text.setVisibility(TextView.VISIBLE);
    }
}