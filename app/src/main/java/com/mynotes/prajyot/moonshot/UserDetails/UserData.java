package com.mynotes.prajyot.moonshot.UserDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class UserData {

    public static String getUserName(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP, Context.MODE_PRIVATE);
        return sp.getString(Finals.USERNAME,"");
    }
    public static void setUserName(Context context,String name){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.USERNAME,name);
        editor.commit();
        Log.e("commited",name);
    }

    public static String getPassword(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP, Context.MODE_PRIVATE);
        return sp.getString(Finals.PASSWORD,"");
    }
    public static void setPassword(Context context,String name){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.PASSWORD,name);
        editor.commit();
        Log.e("commited",name);
    }
}
