package com.gogoship.gogoship.activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gogoship.gogoship.MainActivity;
import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.model.LoginModel;
import com.gogoship.gogoship.response.LoginResponse;
import com.gogoship.gogoship.util.SharedPreference_main;
import com.gogoship.gogoship.utility.Originator;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends Originator {

    ConstraintLayout login, phone_code;
    TextView signup, forget;
    List<String> country_Name_list = new ArrayList<>();
    Spinner spinner_country;
    EditText numOne, numTwo, numThree, numFour, numFive, numSix;
    EditText edt_phone, edt_password;
    String str_phone, str_password;
    SharedPreferences sharedPreferences;
    String code = "", language_key = "";
    LoginResponse loginResponse;
    CheckBox cb_rember;
    boolean check_remember = false;
    SharedPreference_main sharedPreference_main;
    ImageView back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        language_key = sharedPreferences.getString("language_key", "");
        login = (ConstraintLayout) findViewById(R.id.cons_login);
        signup = (TextView) findViewById(R.id.signUpTxt);
        phone_code = findViewById(R.id.phone_code);
        spinner_country = findViewById(R.id.spinner_country_login);
        edt_phone = findViewById(R.id.edt_email_login);
        edt_password = findViewById(R.id.edt_password_login);
        cb_rember = findViewById(R.id.cb_rember);
        forget = findViewById(R.id.txt_forgot);
        back = findViewById(R.id.img_back);
        sharedPreference_main = SharedPreference_main.getInstance(this);

        if (sharedPreference_main.get_rememer_login().equals("true")) {
            edt_phone.setText(sharedPreference_main.get_rememer_userid());
            edt_password.setText(sharedPreference_main.get_rememer_pass());
            cb_rember.setChecked(true);
        } else {
            cb_rember.setChecked(false);
        }
        cb_rember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check_remember = isChecked;
                if (isChecked == true) {
                    sharedPreference_main.set_rememer_login("true");
                } else {
                    sharedPreference_main.set_rememer_login("false");
                }
//                Toast.makeText(Login.this, ""+isChecked, Toast.LENGTH_SHORT).show();
            }
        });
        String[] proviance_array = {"+964", "+91"};
        spinner_country.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_spinner_row, R.id.rowTextView, proviance_array));
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strPhone_Code = spinner_country.getSelectedItem().toString();
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        str_phone = edt_phone.getText().toString();
                        str_password = edt_password.getText().toString();
                        String phone_code = strPhone_Code + "" + str_phone;

                        if (str_phone.equals("")) {
                            Toast toast = Toast.makeText(LoginActivity.this, getString(R.string.please_enter_email), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (str_password.equals("")) {
                            Toast toast = Toast.makeText(LoginActivity.this, getString(R.string.please_enter_password), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            loginApi(str_phone,strPhone_Code);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  Toasty.error(EditProfileActivity.this, "002255555", Toast.LENGTH_SHORT).show();
            }
        });
        signupListener();
        forgetListener();
        backListener();
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void forgetListener() {

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loginApi(String str_phone, String strPhone_Code) {

        //  CustomProgressbar.showProgressBar(LoginActivity.this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<LoginModel> call = service.getLogin(str_phone, str_password, language_key,strPhone_Code);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<LoginModel> call, @NonNull retrofit2.Response<LoginModel> response) {
                //   CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            loginResponse = response.body().getLoginResponse();
                            if (sharedPreference_main.get_rememer_login().equals("true")) {
                                sharedPreference_main.set_rememer_pass(str_password);
                                sharedPreference_main.set_rememer_userid(LoginActivity.this.str_phone);
                            } else {
                                if (check_remember == true) {
                                    sharedPreference_main.set_rememer_login("true");
                                    sharedPreference_main.set_rememer_pass(str_password);
                                    sharedPreference_main.set_rememer_userid(LoginActivity.this.str_phone);
                                } else {
                                    sharedPreference_main.set_rememer_login("false");
                                    sharedPreference_main.set_rememer_pass("");
                                    sharedPreference_main.set_rememer_userid("");
                                }
                            }
                            sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", String.valueOf(loginResponse.getUserId()));
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            //  CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(LoginActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(LoginActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(LoginActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(LoginActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(LoginActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(LoginActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(LoginActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                //  CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<LoginModel> call, Throwable t) {
                //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(LoginActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(LoginActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void phonecodeListener() {
        phone_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void signupListener() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupOtpActivity.class);
                startActivity(intent);
            }
        });
    }
}