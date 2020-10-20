package com.example.dulichhaiphong.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.dulichhaiphong.Activity.HomeActivity;
import com.example.dulichhaiphong.Activity.LoginActivity;
import com.example.dulichhaiphong.Activity.Trangchu;

import java.util.HashMap;


public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String EMAIL = "EMAIL";
    public static final String ID = "ID";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String account, String email, String id){

        editor.putBoolean(LOGIN, true);
        editor.putString(ACCOUNT,account);
        editor.putString(EMAIL, email);
        editor.putString(ID, id);
        editor.apply();

    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((HomeActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(ACCOUNT,sharedPreferences.getString(ACCOUNT,null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Trangchu.class);
        context.startActivity(i);
        ((HomeActivity) context).finish();
    }
}
