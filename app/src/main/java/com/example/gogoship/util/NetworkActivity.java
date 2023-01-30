package com.mobapps.gogoship.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.mobapps.gogoship.R;
import com.mobapps.gogoship.activity.SplashActivity;

import java.util.concurrent.ConcurrentSkipListMap;


public class NetworkActivity extends AppCompatActivity {

    private LinearLayout.LayoutParams layoutParams;
    ConstraintLayout try_again;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_lost_fragment);
       try_again=findViewById(R.id.card_try_again);
       /* LinearLayout linearLayout = new LinearLayout(NetworkActivity.this);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);*/

        /*TextView button1 = new TextView(NetworkActivity.this);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 20;
        button1.setLayoutParams(layoutParams);
        button1.setText(getString(R.string.please_check_your_internet_connection));
        button1.setTextSize(18f);
        button1.setTextColor(ContextCompat.getColor(NetworkActivity.this, R.color.receive));
        linearLayout.addView(button1);*/

       /* Button button = new Button(NetworkActivity.this);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);
        button.setText(getString(R.string.try_again));
        button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        button.setTextColor(getResources().getColor(R.color.white));*/
        try_again.setOnClickListener(v -> {
            Intent intents = new Intent(NetworkActivity.this, SplashActivity.class);
            intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intents);
        });
      //  linearLayout.addView(button);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);

       // setContentView(linearLayout);
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
            Logger.analyser(NetworkActivity.this, Logger.LoggerMessage.EVENT_CALENDER, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "isNetworkAvailable(Originator.this) : " + isNetworkAvailable(NetworkActivity.this)
            );

            if (isNetworkAvailable(NetworkActivity.this)) {
                Intent intents = new Intent(NetworkActivity.this, SplashActivity.class);
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intents);
            }

        }
    };

    private Boolean isNetworkAvailable(Context application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(activeNetwork);
            //return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
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

    public static NetworkActivity.ConnectionQuality getWifiLevel(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null) {
            int linkSpeed = wifiManager.getConnectionInfo().getRssi();
            int level = WifiManager.calculateSignalLevel(linkSpeed, 5);

            if (level == 1) {
                return NetworkActivity.ConnectionQuality.UNKNOWN;
            } else if (level > 7) {
                return NetworkActivity.ConnectionQuality.EXCELLENT;
            } else if (level > 5) {
                return NetworkActivity.ConnectionQuality.GOOD;
            } else if (level > 2) {
                return NetworkActivity.ConnectionQuality.MODERATE;
            } else if (level == 2) {
                return NetworkActivity.ConnectionQuality.POOR;
            }
        }

        return NetworkActivity.ConnectionQuality.UNKNOWN;
    }


}