package com.gogoship.gogoship.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference_main {

    private Context context;
    private static SharedPreference_main sharedPreference_main;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreference;

    public SharedPreference_main(Context context) {
        this.context = context;
        sharedPreference = context.getSharedPreferences("PREF_READ", Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }

    public static SharedPreference_main getInstance(Context context) {
        if (sharedPreference_main == null) {
            sharedPreference_main = new SharedPreference_main(context);
        }
        return sharedPreference_main;
    }

    public String get_user_profile() {
        return sharedPreference.getString("user_profile", "");
    }

    public void set_user_profile(String user_profile) {
        editor.putString("user_profile", user_profile);
        editor.commit();
    }

    public String get_intro() {
        return sharedPreference.getString("intro", "");
    }

    public void set_intro(String intro) {
        editor.putString("intro", intro);
        editor.commit();
    }

    public String get_Auth_token() {
        return sharedPreference.getString("token", "");
    }

    public void set_Auth_token(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String get_userid() {
        return sharedPreference.getString("userid", "");
    }

    public void set_userid(String userid) {
        editor.putString("userid", userid);
        editor.commit();
    }

    public String get_password() {
        return sharedPreference.getString("password", "");
    }

    public void set_password(String userid) {
        editor.putString("password", userid);
        editor.commit();
    }

    public String get_eamil() {
        return sharedPreference.getString("eamil", "");
    }

    public void set_eamil(String eamil) {
        editor.putString("eamil", eamil);
        editor.commit();
    }

    public String get_mobile() {
        return sharedPreference.getString("mobile", "");
    }

    public void set_mobile(String mobile) {
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public String get_name() {
        return sharedPreference.getString("name", "");
    }

    public void set_name(String name) {
        editor.putString("name", name);
        editor.commit();
    }

    public String get_message_likes_notification() {
        return sharedPreference.getString("message_likes_notification", "");
    }

    public void set_message_likes_notification(String message_likes_notification) {
        editor.putString("message_likes_notification", message_likes_notification);
        editor.commit();
    }

    public String get_message_notification() {
        return sharedPreference.getString("message_notification", "");
    }

    public void set_message_notification(String message_notification) {
        editor.putString("message_notification", message_notification);
        editor.commit();
    }

    public String get_new_match_notification() {
        return sharedPreference.getString("new_match_notification", "");
    }

    public void set_new_match_notification(String new_match_notification) {
        editor.putString("new_match_notification", new_match_notification);
        editor.commit();
    }

    public String get_max_age() {
        return sharedPreference.getString("max_age", "");
    }

    public void set_max_age(String max_age) {
        editor.putString("max_age", max_age);
        editor.commit();
    }

    public String get_min_age() {
        return sharedPreference.getString("min_age", "");
    }

    public void set_min_age(String min_age) {
        editor.putString("min_age", min_age);
        editor.commit();
    }

    public String get_distance() {
        return sharedPreference.getString("distance", "");
    }

    public void set_distance(String distance) {
        editor.putString("distance", distance);
        editor.commit();
    }

    public String get_show_me() {
        return sharedPreference.getString("show_me", "");
    }

    public void set_show_me(String show_me) {
        editor.putString("show_me", show_me);
        editor.commit();
    }

    public String get_gender() {
        return sharedPreference.getString("gender", "");
    }

    public void set_gender(String gender) {
        editor.putString("gender", gender);
        editor.commit();
    }

    public String get_account_status() {
        return sharedPreference.getString("account_status", "");
    }

    public void set_account_status(String account_status) {
        editor.putString("account_status", account_status);
        editor.commit();
    }

    public String get_DToken() {
        return sharedPreference.getString("DToken", "");
    }

    public void set_DToken(String DToken) {
        editor.putString("DToken", DToken);
        editor.commit();
    }

    public String get_prmission() {
        return sharedPreference.getString("prmission", "");
    }

    public void set_prmission(String prmission) {
        editor.putString("prmission", prmission);
        editor.commit();
    }

    public String get_checklogin_via() {
        return sharedPreference.getString("checklogin_via", "");
    }

    public void set_checklogin_via(String checklogin_via) {
        editor.putString("checklogin_via", checklogin_via);
        editor.commit();
    }


    public String get_merital_status() {
        return sharedPreference.getString("merital_status", "");
    }

    public void set_merital_status(String merital_status) {
        editor.putString("merital_status", merital_status);
        editor.commit();
    }

    public String get_rememer_login() {
        return sharedPreference.getString("rememer_login", "");
    }

    public void set_rememer_login(String rememer_login) {
        editor.putString("rememer_login", rememer_login);
        editor.commit();
    }

    public String get_rememer_userid() {
        return sharedPreference.getString("ruserid", "");
    }

    public void set_rememer_userid(String ruserid) {
        editor.putString("ruserid", ruserid);
        editor.commit();
    }

    public String get_rememer_pass() {
        return sharedPreference.getString("r_pass", "");
    }

    public void set_rememer_pass(String r_pass) {
        editor.putString("r_pass", r_pass);
        editor.commit();
    }

    public String get_version_code() {
        return sharedPreference.getString("version_code", "");
    }

    public void set_version_code(String version_code) {
        editor.putString("version_code", version_code);
        editor.commit();
    }

}