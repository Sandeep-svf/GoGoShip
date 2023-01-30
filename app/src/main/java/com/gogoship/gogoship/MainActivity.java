package com.gogoship.gogoship;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.activity.LanguageActivity;
import com.gogoship.gogoship.activity.LoginSignupActivity;
import com.gogoship.gogoship.fragment.ArchiveFragment;
import com.gogoship.gogoship.fragment.ChangePassActivity;
import com.gogoship.gogoship.fragment.CurrancyFragment;
import com.gogoship.gogoship.fragment.DashboardFragment;
import com.gogoship.gogoship.fragment.MyOrderFragment;
import com.gogoship.gogoship.fragment.MyProfileFragment;
import com.gogoship.gogoship.fragment.NewOrderFragment;
import com.gogoship.gogoship.fragment.NotificationFragment;
import com.gogoship.gogoship.fragment.SupportFragment;
import com.gogoship.gogoship.fragment.WalletFragment;
import com.gogoship.gogoship.model.ProfileModel;
import com.gogoship.gogoship.response.ProfileDataResponse;
import com.gogoship.gogoship.util.SharedPreference_main;
import com.gogoship.gogoship.utility.CustomProgressbar;
import com.gogoship.gogoship.utility.DashboardOriginator;
import com.gogoship.gogoship.utility.Logger;
import com.google.gson.JsonSyntaxException;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity  extends DashboardOriginator {
    private int mRequestCode = 100;
    public MeowBottomNavigation bottomNav;
    ImageView btnMenuImage, notification;
    RelativeLayout my_profile, my_orders, change_pass, my_currancy, support, my_dashboard, archive, change_language, my_wallet, login, logout;
    private MainActivity context;
    DashboardFragment dashboardFragment;
    NewOrderFragment newOrderFragment;
    WalletFragment walletFragment;
    ConstraintLayout btn_menu;
    AlertDialog dialogs;
    SharedPreference_main sharedPreference_main;
    int doubleBackToExitPressed = 1;
    public static int navItemIndex = 0;
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sharedPreferences;
    String language_key = "";
    String user_id = "";
    ProfileDataResponse profileDataResponse;
    ImageView image_menu;
    TextView userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.activity_menu);

        context = MainActivity.this;
        //  Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(context));
        bottomNav = (MeowBottomNavigation) findViewById(R.id.bottomNav);
        btnMenuImage = (ImageView) findViewById(R.id.toolbar_menu_image);
        btn_menu = (ConstraintLayout) findViewById(R.id.cons_btn_menu);
        my_profile = (RelativeLayout) findViewById(R.id.relative_my_profile);
        my_orders = (RelativeLayout) findViewById(R.id.relative_my_order);
        change_pass = (RelativeLayout) findViewById(R.id.relative_change_password);
        my_currancy = (RelativeLayout) findViewById(R.id.relative_currancy);
        support = (RelativeLayout) findViewById(R.id.relative_support);
        my_dashboard = (RelativeLayout) findViewById(R.id.relative_dashboard);
        notification = (ImageView) findViewById(R.id.notification_logo);
        archive = (RelativeLayout) findViewById(R.id.relative_archive);
        my_wallet = (RelativeLayout) findViewById(R.id.relative_my_wallet);
        login = (RelativeLayout) findViewById(R.id.relative_login);
        change_language = (RelativeLayout) findViewById(R.id.relative_change_language);
        logout = (RelativeLayout) findViewById(R.id.relative_logout);
        userName = (TextView) findViewById(R.id.userNameTxt);
        image_menu = (ImageView) findViewById(R.id.profile_image_menu);
        sharedPreference_main = SharedPreference_main.getInstance(this);
        sharedPreferences = this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        language_key = sharedPreferences.getString("language_key", "");
        user_id = sharedPreferences.getString("user_id", "");
        if (user_id.equals("")) {
            login.setVisibility(View.VISIBLE);
            my_dashboard.setVisibility(View.VISIBLE);
        } else {
            my_dashboard.setVisibility(View.VISIBLE);
            my_orders.setVisibility(View.VISIBLE);
            my_wallet.setVisibility(View.VISIBLE);
            archive.setVisibility(View.VISIBLE);
            my_profile.setVisibility(View.VISIBLE);
            my_currancy.setVisibility(View.VISIBLE);
            support.setVisibility(View.VISIBLE);
            my_currancy.setVisibility(View.VISIBLE);
            change_pass.setVisibility(View.VISIBLE);
            change_language.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        }
        newOrderFragment = new NewOrderFragment();
        dashboardFragment = new DashboardFragment();
        walletFragment = new WalletFragment();

        bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home_24));
        bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_add_24));
        bottomNav.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_account_balance_wallet_24));

        bottomNav.show(1, true);
        profileApi();
        bottomNav.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                int i = model.getId();

                switch (i) {
                    case 1:

                        loadFragment(dashboardFragment);
                        break;

                    case 2:

                        loadFragment(newOrderFragment);
                        break;

                    case 3:

                        loadFragment(walletFragment);
                        break;
                    //...other cases
                }
                return Unit.INSTANCE;
            }
        });

        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            final Display display = getWindowManager().getDefaultDisplay();
            int screenWidth = display.getWidth();
            final int slidingmenuWidth = (int) (screenWidth - (screenWidth / 3.7) + 23);
            final int offset = Math.max(0, screenWidth - slidingmenuWidth);
            getSlidingMenu().setBehindOffset(offset);
            if (language_key.equals("ar")) {
                getSlidingMenu().setMode(SlidingMenu.RIGHT);
            } else if (language_key.equals("ku")) {
                getSlidingMenu().setMode(SlidingMenu.RIGHT);
            }

        } catch (Exception e) {
            Logger.analyser(context, Logger.LoggerMessage.Outfit_List_Fragment101, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    e
            );
        }

        btnMenuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showMenu();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (NullPointerException e) {
                    Log.e("exception", "     " + e);
                }
            }
        });

        notificationListener();
        setupNavigationView();
    }

    private void notificationListener() {

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setVisibility(View.INVISIBLE);
                NotificationFragment notificationFragment = new NotificationFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, notificationFragment);
                transaction.commit();
            }
        });

    }

    private void setupNavigationView() {

        String terms_key = getIntent().getStringExtra("login_key");
        String web_key = getIntent().getStringExtra("web_key");


        if (terms_key != null) {
            if (terms_key.equals("web_value")) {

                String prod_url = getIntent().getStringExtra("prod_url");
                String prod_name = getIntent().getStringExtra("prod_name");
                String prod_price = getIntent().getStringExtra("prod_price");
                String prod_img = getIntent().getStringExtra("prod_img");
                Log.e("chk_val", "      " + prod_url + "       " + prod_name + "       " + prod_price);
                bottomNav.show(2, true);
                NewOrderFragment newOrderFragment = new NewOrderFragment();
                Bundle args = new Bundle();
                args.putString("main_act_key", "main_act_value");
                args.putString("notification_key", "notification_main");
                args.putString("main_prod_price", prod_price);
                args.putString("main_prod_url", prod_url);
                args.putString("main_prod_name", prod_name);
                args.putString("prod_img", prod_img);
                newOrderFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, newOrderFragment);
                transaction.commit();
                getSlidingMenu().toggle();

            }
        }

        if (terms_key.equals("login_value")) {
            //  bottomNav.show(1, true);
            bottomNav.setVisibility(View.VISIBLE);
            DashboardFragment dashboardFragment = new DashboardFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment_container, dashboardFragment);
            transaction.commit();
        }

        my_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (NullPointerException e) {
                    Log.e("exception", "     " + e);
                }

                bottomNav.setVisibility(View.VISIBLE);
                bottomNav.show(1, true);
                DashboardFragment dashboardFragment = new DashboardFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, dashboardFragment);
                transaction.commit();
                getSlidingMenu().toggle();
            }
        });

        my_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setVisibility(View.VISIBLE);
                bottomNav.show(3, true);
                WalletFragment walletFragment = new WalletFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, walletFragment);
                transaction.commit();
                getSlidingMenu().toggle();
            }
        });

        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setVisibility(View.GONE);
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, myProfileFragment);
                transaction.commit();
                getSlidingMenu().toggle();

            }
        });


        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setVisibility(View.GONE);
                MyOrderFragment myOrderFragment = new MyOrderFragment();
                Bundle args = new Bundle();
                args.putString("notification_key", "notification_main");
                myOrderFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, myOrderFragment);
                transaction.commit();
                getSlidingMenu().toggle();
            }
        });


        my_currancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setVisibility(View.GONE);
                CurrancyFragment currancyFragment = new CurrancyFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, currancyFragment);
                transaction.commit();
                getSlidingMenu().toggle();
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setVisibility(View.GONE);
                SupportFragment supportFragment = new SupportFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, supportFragment);
                transaction.commit();
                getSlidingMenu().toggle();
            }
        });


        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChangePassActivity.class);
                startActivity(intent);
            }
        });

        change_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LanguageActivity.class);
                intent.putExtra("from_main", "chang_lang");
                startActivity(intent);
            }
        });

        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setVisibility(View.GONE);
                ArchiveFragment archiveFragment = new ArchiveFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, archiveFragment);
                transaction.commit();
                getSlidingMenu().toggle();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout_AlertDialog();
            }
        });
    }

    private void Logout_AlertDialog() {

        final LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.logout_dialog, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnYes = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btnNo = alertLayout.findViewById(R.id.btn_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(this.getString(R.string.confirm_logout));
        tvMessage.setText(this.getString(R.string.are_you_sure_to_logout));
        alert.setView(alertLayout);
        alert.setCancelable(false);



        btnYes.setOnClickListener(v -> {
            dialogs.dismiss();
            //  relative_logout.setEnabled(true);
            sharedPreference_main.set_userid("");
            SharedPreferences preferences = MainActivity.this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
            preferences.edit().remove("user_id").apply();
            Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
            startActivity(intent);
            finish();

        });

        btnNo.setOnClickListener(v -> {
            dialogs.dismiss();
            // relative_logout.setEnabled(true);
        });

        dialogs = alert.create();
        dialogs.show();

    }

    private void loadFragment(WalletFragment walletFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_container, walletFragment);
        transaction.commit();

    }

    private void loadFragment(NewOrderFragment newOrderFragment) {
        Bundle args = new Bundle();
        args.putString("main_act_key", "main_act_value1");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_container, newOrderFragment);
        newOrderFragment.setArguments(args);
        transaction.commit();

    }

    private void loadFragment(DashboardFragment dashboardFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_container, dashboardFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        boolean shouldLoadHomeFragOnBackPress = true;
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                //  CURRENT_TAG = TAG_DASHBOARD;
                loadHomeFragment();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.back_exit, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
                return;
            }
        }
    }

    private void loadHomeFragment() {
        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_container, dashboardFragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && resultCode == RESULT_OK) {
            String key = data.getStringExtra("webview_activity");

            if (key.equals("web_key")) {
                if (language_key.equals("en")) {

                    Locale locale2 = new Locale("en");
                    Locale.setDefault(locale2);
                    Configuration config2 = new Configuration();
                    config2.locale = locale2;
                    getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());


                } else if (language_key.equals("ar")) {
                    Locale locale2 = new Locale("ar");
                    Locale.setDefault(locale2);
                    Configuration config2 = new Configuration();
                    // config2.locale = locale2;
                    config2.setLocale(locale2);

                    //  WebsiteWebviewActivity.this.getBaseContext().getResources().updateConfiguration(config2, WebsiteWebviewActivity.this.getBaseContext().getResources().getDisplayMetrics());
                    context.getResources().updateConfiguration(config2,
                            context.getResources().getDisplayMetrics());

                } else if (language_key.equals("ku")) {
                    Locale locale2 = new Locale("ur");
                    Locale.setDefault(locale2);
                    Configuration config2 = new Configuration();
                    config2.locale = locale2;
                    getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());


                }
            }
        }
    }

    private void profileApi() {
        // CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ProfileModel> call = service.getProfile(user_id, language_key);
        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProfileModel> call, @NonNull retrofit2.Response<ProfileModel> response) {
                //  CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            // Toast.makeText(MainActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            profileDataResponse = response.body().getProfileDataResponse();
                            //  Glide.with(profile_image.getContext()).load(profileDataResponse.getProfileImage()).into(profile_image);
                            Glide.with(MainActivity.this).load(profileDataResponse.getProfileImage()).placeholder(R.drawable.imgusr).into(image_menu);
                            userName.setText("" + profileDataResponse.getName());
                        } else {
                            // Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(MainActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(MainActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(MainActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(MainActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(MainActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(MainActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(MainActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException exception) {
                    exception.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<ProfileModel> call, Throwable t) {
                //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(MainActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


