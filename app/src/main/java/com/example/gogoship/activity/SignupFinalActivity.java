package com.mobapps.gogoship.activity;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobapps.gogoship.MainActivity;
import com.mobapps.gogoship.R;
import com.mobapps.gogoship.RetrofitApi.APIService;
import com.mobapps.gogoship.RetrofitApi.ApiClient;
import com.mobapps.gogoship.model.ProfileModel;
import com.mobapps.gogoship.model.ProvianceModel;
import com.mobapps.gogoship.model.SignupModel;
import com.mobapps.gogoship.response.ProvianceResponse;
import com.mobapps.gogoship.response.SignupTempResponse;
import com.mobapps.gogoship.utility.CustomProgressbar;
import com.mobapps.gogoship.utility.Originator;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SignupFinalActivity extends Originator {

    ConstraintLayout finish;
    LinearLayout gender, proviance;
    EditText edt_gender, edt_address, edt_landmark, edt_password;
    Spinner sp_gender, sp_proviance;
    String email = "", name = "", phone = "";
    String visible_gender = "1", str_gender = "", lookin_for_id_visivible = "1",countru_code="";
    String str_address = "", str_landmark = "", str_password = "";
    String str_proviance = "";
    List<ProvianceResponse> profileModelArrayList = new ArrayList<>();
    List<String> provianceList = new ArrayList<>();
    List<String> genderList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "";
    SignupTempResponse signupTempResponse;
    ConstraintLayout back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_detail);

        finish = (ConstraintLayout) findViewById(R.id.card_reg_final);
        gender = findViewById(R.id.gender_dropdown);
        proviance = findViewById(R.id.province_dropdown);
        // edt_gender=findViewById(R.id.edt_reg_det_gender);
        sp_gender = (Spinner) findViewById(R.id.sp_gender);
        sp_proviance = (Spinner) findViewById(R.id.sp_proviance);
        edt_address = findViewById(R.id.edt_reg_detail_add);
        edt_landmark = findViewById(R.id.edt_reg_det_landmark);
        edt_password = findViewById(R.id.edt_reg_det_pass);
        back = findViewById(R.id.cons_main_toolbar_final);
        email = getIntent().getStringExtra("str_Email");
        name = getIntent().getStringExtra("str_name");
        countru_code = getIntent().getStringExtra("country_code_phone");
        phone = getIntent().getStringExtra("str_phone");
        sharedPreferences = this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        language_key = sharedPreferences.getString("language_key", "");
        provianceApi();
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp_gender.performClick();
            }
        });

        proviance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp_proviance.performClick();
            }
        });

        genderList.add("Male");
        genderList.add("Female");
        sp_gender.setAdapter(new ArrayAdapter<String>(SignupFinalActivity.this, R.layout.simple_spinner_edit_pro_row, R.id.rowTextView, genderList));
        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                str_gender = genderList.get(position).toString();
                Log.e("gender_check", "      " + str_gender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_address = edt_address.getText().toString();
                str_landmark = edt_landmark.getText().toString();
                str_password = edt_password.getText().toString();

                if (str_address.equals("")) {
                    Toast toast = Toast.makeText(SignupFinalActivity.this, getString(R.string.please_enter_address), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (str_landmark.equals("")) {
                    Toast toast = Toast.makeText(SignupFinalActivity.this, getString(R.string.please_enter_landmark), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (str_password.equals("")) {
                    Toast toast = Toast.makeText(SignupFinalActivity.this, getString(R.string.please_enter_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    signupApi();
                }
            }
        });
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

    private void provianceApi() {

        //    CustomProgressbar.showProgressBar(this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ProvianceModel> call = service.getProviance(user_id);
        call.enqueue(new Callback<ProvianceModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProvianceModel> call, @NonNull retrofit2.Response<ProvianceModel> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            Toast.makeText(SignupFinalActivity.this, "proviance list", Toast.LENGTH_SHORT).show();
                            provianceList.clear();
                            profileModelArrayList = response.body().getProvianceResponseList();
                            for (int i = 0; i < profileModelArrayList.size(); i++) {
                                provianceList.add(profileModelArrayList.get(i).getProvinceName());
                            }

                            sp_proviance.setAdapter(new ArrayAdapter<String>(SignupFinalActivity.this, R.layout.simple_spinner_row, R.id.rowTextView, provianceList));
                            sp_proviance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    str_proviance = provianceList.get(position).toString();
                                    Log.e("prov_check", "    " + str_proviance);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    //  Toasty.error(EditProfileActivity.this, "002255555", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(SignupFinalActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        try {
                            //    CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignupFinalActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignupFinalActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignupFinalActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignupFinalActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignupFinalActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignupFinalActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignupFinalActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignupFinalActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ProvianceModel> call, Throwable t) {
                //    CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(SignupFinalActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignupFinalActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signupApi() {
        //  CustomProgressbar.showProgressBar(SignupFinalActivity.this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<SignupModel> call = service.getSignup(name,
                phone,
                str_password,
                email,
                str_proviance,
                str_landmark,
                str_address,
                str_gender,
                language_key,
                countru_code);

        Log.e("test_sam_check_value",countru_code);

        call.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SignupModel> call, @NonNull retrofit2.Response<SignupModel> response) {
                //   CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            Toast.makeText(SignupFinalActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            // Log.e("signup_user_id","     "+signupTempResponse.getUserId());

                            Intent intent = new Intent(SignupFinalActivity.this, LoginActivity.class);
                            // intent.putExtra("login_key", "login_value");
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignupFinalActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            //  CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignupFinalActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignupFinalActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignupFinalActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignupFinalActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignupFinalActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignupFinalActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignupFinalActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignupFinalActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<SignupModel> call, Throwable t) {
                //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(SignupFinalActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignupFinalActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}