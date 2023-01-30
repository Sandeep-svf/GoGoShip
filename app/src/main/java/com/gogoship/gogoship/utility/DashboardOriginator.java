package com.gogoship.gogoship.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gogoship.gogoship.util.NetworkActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.Locale;

public abstract class DashboardOriginator extends SlidingFragmentActivity {

    public DashboardOriginator contextOriginator;
    private SharedPreferences sharedPreferencesLanguageName;
    private String lang_txt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextOriginator = DashboardOriginator.this;

        sharedPreferencesLanguageName = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        if (sharedPreferencesLanguageName.getString("language_text", "").equals("en")) {
            lang_txt = "en";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ar")) {
            lang_txt = "ar";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ku")) {
            lang_txt = "ku";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ur")) {
            lang_txt = "ur";
        }else if (sharedPreferencesLanguageName.getString("language_text", "").equals("zh")) {
            lang_txt = "zh";
        }

        Locale locale2 = new Locale(lang_txt);
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.locale = locale2;
        getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());


        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);


    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception e) {

        }
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (status(context) == ConnectivityManager.TYPE_MOBILE) {

                /*if (!(contextOriginator instanceof Splash_Activity)) {
                    Intent intents = new Intent(Originator.this, Splash_Activity.class);
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intents);
                }*/

            } else if (status(context) == ConnectivityManager.TYPE_WIFI) {

                if (getWifiLevel(context) == ConnectionQuality.POOR || getWifiLevel(context) == ConnectionQuality.UNKNOWN) {

                    Intent intents = new Intent(DashboardOriginator.this, NetworkActivity.class);
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intents);


                } else {
                    /*if (!(contextOriginator instanceof Splash_Activity)) {
                        Intent intents = new Intent(Originator.this, Splash_Activity.class);
                        intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intents);
                    }*/
                }
            } else if (status(context) == -1) {

                Intent intents = new Intent(DashboardOriginator.this, NetworkActivity.class);
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intents);

            }

        }
    };

    public static int status(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        int status = -1;

        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                status = ConnectivityManager.TYPE_WIFI;
            }

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = ConnectivityManager.TYPE_MOBILE;
            }
        }

        return status;
    }


    public static ConnectionQuality getWifiLevel(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null) {
            int linkSpeed = wifiManager.getConnectionInfo().getRssi();
            int level = WifiManager.calculateSignalLevel(linkSpeed, 5);

            if (level == 1) {
                return ConnectionQuality.UNKNOWN;
            } else if (level > 7) {
                return ConnectionQuality.EXCELLENT;
            } else if (level > 5) {
                return ConnectionQuality.GOOD;
            } else if (level > 2) {
                return ConnectionQuality.MODERATE;
            } else if (level == 2) {
                return ConnectionQuality.POOR;
            }
        }

        return ConnectionQuality.UNKNOWN;
    }

    public enum ConnectionQuality {
        /**
         * Bandwidth under 150 kbps.
         */
        POOR,
        /**
         * Bandwidth between 150 and 550 kbps.
         */
        MODERATE,
        /**
         * Bandwidth between 550 and 2000 kbps.
         */
        GOOD,
        /**
         * EXCELLENT - Bandwidth over 2000 kbps.
         */
        EXCELLENT,
        /**
         * Placeholder for unknown bandwidth. This is the initial value and will stay at this value
         * if a bandwidth cannot be accurately found.
         */
        UNKNOWN
    }
}
