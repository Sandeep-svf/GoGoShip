package com.mobapps.gogoship.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobapps.gogoship.util.NetworkActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.Locale;

public class Originator  extends AppCompatActivity {

    public static Originator contextOriginator;
    private SharedPreferences sharedPreferencesLanguageName;
    private String lang_txt;
    private String str_lanuage;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextOriginator = Originator.this;

     //   backCreate();

     //   decimalFormat = new DecimalFormat("#.##");

        sharedPreferencesLanguageName = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        if (sharedPreferencesLanguageName.getString("language_text", "").equals("en")) {
            lang_txt = "en";
            str_lanuage = "1";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ar")) {
            lang_txt = "ar";
        }else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ku")) {
            lang_txt = "ku";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ur")) {
            lang_txt = "ur";
        }else {
            lang_txt = "en";
            str_lanuage = "1";
            SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
            editor.putString("language_text", lang_txt);
            editor.putString("language_id", str_lanuage);
            editor.apply();
        }

        Locale locale2 = new Locale(lang_txt);
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.setLocale(locale2);
        config2.setLayoutDirection(locale2);
        getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);

       // ui();
    }

    private Context updateBaseContextLocale(Context context) {
        sharedPreferencesLanguageName = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        if (sharedPreferencesLanguageName.getString("language_text", "").equals("en")) {
            lang_txt = "en";
            str_lanuage = "1";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("iw")) {
            lang_txt = "iw";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ru")) {
            lang_txt = "ru";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("zh")) {
            lang_txt = "zh";
        } else {
            lang_txt = "en";
            str_lanuage = "1";
            SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
            editor.putString("language_text", lang_txt);
            editor.putString("language_id", str_lanuage);
            editor.apply();
        }

        Locale locale = new Locale(lang_txt);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            return updateResourcesLocale(context, locale);
        }

        return updateResourcesLocaleLegacy(context, locale);
    }

    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.analyser(contextOriginator, Logger.LoggerMessage.EVENT_CALENDER, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "isNetworkAvailable(Originator.this) : " + isNetworkAvailable(Originator.this)
            );

            if (!isNetworkAvailable(Originator.this)) {
                Intent intents = new Intent(Originator.this, NetworkActivity.class);
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
}