package com.secondapp.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Signup extends AppCompatActivity {
    EditText firstName,lastName,ages,heigh,weigh;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button signup;


    //shared pref;

    public static final String MyPREFERENCES = "MyData" ;
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String SEX = "sex";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";


    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        firstName=findViewById(R.id.firstname);
        lastName=findViewById(R.id.lastname);
        radioGroup=findViewById(R.id.radio);
        ages=findViewById(R.id.age);
        heigh=findViewById(R.id.height);
        weigh=findViewById(R.id.weight);
        signup=findViewById(R.id.signup);
        preferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //initialization;
        if (isSignedUp()){
            Intent intent =new Intent(Signup.this,MainActivity.class);
            startActivity(intent);
            finish();
        }






    }
    public void check(){
        if (firstName.getText().toString().isEmpty())
            firstName.setError("please enter first name");
        if (lastName.getText().toString().isEmpty())
            lastName.setError("please enter last name");
        if (ages.getText().toString().isEmpty())
            ages.setError("please enter age name");
        if (heigh.getText().toString().isEmpty())
            heigh.setError("please enter heigth name");
        if (weigh.getText().toString().isEmpty())
            weigh.setError("please enter weight name");




    }
    public void onClick(View view){
        check();


        String name=firstName.getText().toString()+" "+lastName.getText().toString();
        try {
            int selectedId=radioGroup.getCheckedRadioButtonId();
            if (selectedId==-1)  Toast.makeText(this,"Select Gender",Toast.LENGTH_SHORT).show();
            radioButton=findViewById(selectedId);
            String gender=radioButton.getText().toString();
            int age= Integer.parseInt(ages.getText().toString());
            float height= Integer.parseInt(heigh.getText().toString());
            float weight= Integer.parseInt(weigh.getText().toString());

            SharedPreferences.Editor editor=preferences.edit();
            editor.putString(NAME,name);
            editor.putInt(AGE,age);
            editor.putString(SEX,gender);
            editor.putFloat(HEIGHT,height);
            editor.putFloat(WEIGHT,weight);
            editor.putBoolean("signedup", Boolean.parseBoolean("true"));
            editor.commit();
            Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(Signup.this,MainActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public boolean isSignedUp(){
        boolean signedin=preferences.getBoolean("signedup", Boolean.parseBoolean("false"));

        return signedin;

    }

}