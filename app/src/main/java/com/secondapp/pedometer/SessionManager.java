package com.secondapp.pedometer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE=0;

    private static final String PREF_NAME = "MyDATA";
    private static final String NAME = "name";
    public static final String AGE = "age";
    public static final String SEX = "sex";
    public static final String HEIGHT="height";
    public static final String WEIGHT = "weight";
    public static final String IMAGE_PATH = "image_path";
    public static final String IS_LOGIN = "isLogin";


    public SessionManager(Context _context) {
        this._context = _context;
        pref=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }
    public void createLoginSession(String name, String age,String sex,String height,String weight,String image){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(AGE, age);
        editor.putString(SEX, sex);
        editor.putString(HEIGHT, height);
        editor.putString(WEIGHT, weight);
        editor.putString(IMAGE_PATH, image);
        // commit changes
        editor.commit();
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(NAME, pref.getString(NAME, null));
        user.put(AGE, pref.getString(AGE, null));
        user.put(SEX, pref.getString(SEX, null));
        user.put(HEIGHT, pref.getString(HEIGHT, null));
        user.put(WEIGHT, pref.getString(WEIGHT, null));
        user.put(IMAGE_PATH, pref.getString(IMAGE_PATH, null));

        // return user
        return user;
    }


}
