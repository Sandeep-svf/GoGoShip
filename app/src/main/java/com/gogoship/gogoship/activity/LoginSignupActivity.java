package com.gogoship.gogoship.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.gogoship.gogoship.R;
import com.gogoship.gogoship.utility.Originator;

public class LoginSignupActivity extends Originator {

    ConstraintLayout cons_login,cons_signup;
    int doubleBackToExitPressed = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_signup);

        cons_login= (ConstraintLayout) findViewById(R.id.cons_login);
        cons_signup= (ConstraintLayout) findViewById(R.id.cons_signup);
        cons_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        cons_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSignupActivity.this,SignupOtpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(this, R.string.back_exit, Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }
}