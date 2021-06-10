package com.secondapp.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener{
    private TextView TvSteps,names;
    private TextView calories;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    Button BtnStart,BtnReset;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private double calBurn;
    Signup signup;
    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps =  findViewById(R.id.tv_steps);
        BtnStart = findViewById(R.id.btn_start);
        names=findViewById(R.id.name);
        BtnReset =  findViewById(R.id.btn_stop);
        calories=findViewById(R.id.cal);

        manager=new SessionManager(getApplicationContext());
        if (manager.isLoggedIn()){
            HashMap<String, String> user=manager.getUserDetails();
            String name=user.get(SessionManager.NAME);
            names.setText(name);
        }



        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);






            }
        });


        BtnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sensorManager.unregisterListener(MainActivity.this);
                TvSteps.setText("");
                calories.setText("");

            }
        });



    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(numSteps+"");

            calBurn=numSteps*0.04;
            calories.setText(new DecimalFormat("##.##").format(calBurn));


    }
    }
