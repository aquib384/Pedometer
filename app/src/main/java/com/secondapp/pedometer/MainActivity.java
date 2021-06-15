package com.secondapp.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener{
    private TextView steps,distance,exercise,totalcal,prog;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    Button BtnStart,BtnReset;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    SessionManager sessionManager;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        progressBar=findViewById(R.id.progressBar);
        prog=findViewById(R.id.progress);
        Objects.requireNonNull(getSupportActionBar()).hide();



        steps =  findViewById(R.id.steps);
        distance=findViewById(R.id.distance);
        exercise=findViewById(R.id.exercise);
        totalcal=findViewById(R.id.kcal);
        BtnStart = findViewById(R.id.start);
        sessionManager=new SessionManager(getApplicationContext());
        /**
         *  if (sessionManager.isLoggedIn()){
         *             HashMap<String, String> user=sessionManager.getUserDetails();
         *             String name=user.get(SessionManager.NAME);
         *             names.setText(name);
         *         }
          */



        BtnStart.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);








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

    @SuppressLint("SetTextI18n")
    @Override
    public void step(long timeNs) {
        numSteps++;
        steps.setText(numSteps+"");
        double dist=  (numSteps/1312.33595801);
        distance.setText(new DecimalFormat("#0.00").format(dist)+" km");
        double cal=numSteps*0.04;
        exercise.setText(new DecimalFormat("#0.00").format(cal)+" kcal");
        totalcal.setText( new DecimalFormat("#0.00").format(cal)+" kcal");
        updateProgressBar(numSteps);





    }
    @SuppressLint("SetTextI18n")
    private void updateProgressBar(int progress){
        progressBar.setMax(1000);
        progressBar.setProgress(progress);
        prog.setText((progress*100)/100+"%");
    }
    }
