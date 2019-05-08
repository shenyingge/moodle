package com.example.ks.moodle.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private static SharedPreferenceUtil util;
    private static SharedPreferences preferences;

    public SharedPreferenceUtil(Context context, String name){
        preferences=context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    public static void getPreference(Context context,String name){
        if(util==null){
            util=new SharedPreferenceUtil(context,name);
        }
    }

    /**
     * 存储string类型的数据
     * @param key
     * @param value
     */
    public static void putString(String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value).commit();
    }

    /**
     * 获取String类型的数据
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String key, String defaultValue){
        return preferences.getString(key, defaultValue);
    }

}
