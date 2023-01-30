package com.mobapps.gogoship.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mobapps.gogoship.MainActivity;
import com.mobapps.gogoship.R;
import com.mobapps.gogoship.utility.Originator;
import com.mobapps.gogoship.utility.Singleton;

import java.util.Locale;

public class LanguageActivity extends Originator {

    ConstraintLayout eng_lang, arabic_lang, kurdish_language;
    String str_lanuage = "1";
    Singleton singleton = Singleton.getInstance();
    SharedPreferences sharedPreferencesLanguageName;
    SharedPreferences sharedPreferences1;
    Context context;
    String lang_key = "";
    ImageView back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_layout);

        context = LanguageActivity.this;
        lang_key = getIntent().getStringExtra("from_main");
        eng_lang = (ConstraintLayout) findViewById(R.id.cons_lang_eng);
        arabic_lang = (ConstraintLayout) findViewById(R.id.cons_lang_Arabic);
        back = findViewById(R.id.img_back);
        kurdish_language = (ConstraintLayout) findViewById(R.id.cons_kurdish_lang);
        if (lang_key != null) {
            back.setVisibility(View.VISIBLE);
        }
        arabic_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale2 = new Locale("ar");
                Locale.setDefault(locale2);
                Configuration config2 = new Configuration();
                config2.locale = locale2;
                getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());
                str_lanuage = "2";
                singleton.language_name = "ar";
                sharedPreferencesLanguageName = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
                editor.putString("language_text", "ar");
                editor.putString("language_key", "ar");
                editor.putString("language_id", str_lanuage);
                editor.apply();

                Intent intents = new Intent(context, MainActivity.class);
                intents.putExtra("login_key", "login_value");
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intents);

            }
        });

        kurdish_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Locale locale2 = new Locale("ur");
                Locale.setDefault(locale2);
                Configuration config2 = new Configuration();
                config2.locale = locale2;
                getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());

                str_lanuage = "3";
                singleton.language_name = "ur";
                sharedPreferencesLanguageName = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
                editor.putString("language_text", "ur");
                editor.putString("language_key", "ku");
                editor.putString("language_id", str_lanuage);
                editor.apply();

                Intent intents = new Intent(context, MainActivity.class);
                intents.putExtra("login_key", "login_value");
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intents);

            }
        });


        eng_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Locale locale2 = new Locale("en");
                Locale.setDefault(locale2);
                Configuration config2 = new Configuration();
                config2.locale = locale2;
                getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());

                str_lanuage = "1";
                singleton.language_name = "en";
                sharedPreferencesLanguageName = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
                editor.putString("language_text", "en");
                editor.putString("language_key", "en");
                editor.putString("language_id", str_lanuage);
                editor.apply();

                Intent intents = new Intent(context, MainActivity.class);
                intents.putExtra("login_key", "login_value");
                intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intents);
            }
        });
        bacListener();
    }

    private void bacListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}