package com.fahimshariar.fahimshariar;

import android.app.Application;

import com.fahimshariar.fahimshariar.util.PreferenceUtil;
import com.ldt.fahimshariar.common.AppStartup;
import com.fahimshariar.fahimshariar.util.PreferenceUtil;


public class App extends Application {
    private static App mInstance;
    public static synchronized App getInstance() {
        return mInstance;
    }

    public PreferenceUtil getPreferencesUtility() {
        return PreferenceUtil.getInstance(App.this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppStartup.onAppStartup();
    }


}