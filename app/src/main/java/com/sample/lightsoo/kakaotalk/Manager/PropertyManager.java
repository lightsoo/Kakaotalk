package com.sample.lightsoo.kakaotalk.Manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sample.lightsoo.kakaotalk.MyApplication;

public class PropertyManager {

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private static final String KEY_LOGIN_TYPE = "key_login_type";
    private static final String FILED_KAKAO_ID ="kakao";

    public static final String LOGIN_TYPE_KAKAO = "login_type_kakao";

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }
    // singleton holder pattern : thread safe, lazy class initialization, memory saving.
    public static class InstanceHolder{ private static final PropertyManager INSTANCE = new PropertyManager();}
    public static PropertyManager getInstance(){ return InstanceHolder.INSTANCE; }


    public void setUserLoginId(String id){
        mEditor.putString(FILED_KAKAO_ID, id);
        mEditor.commit();
    }

    public void setLoginType(String loginType){
        mEditor.putString(KEY_LOGIN_TYPE, loginType);
        mEditor.commit();
    }

    public String getUserLoginId(){
        return mPrefs.getString(FILED_KAKAO_ID, "");
    }

    public String getLoginType(){
        return mPrefs.getString(KEY_LOGIN_TYPE, "");
    }


}
