package com.gogoship.gogoship.activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.model.VarifyEmailModel;
import com.gogoship.gogoship.model.VarifyModel;
import com.gogoship.gogoship.utility.CustomProgressbar;
import com.gogoship.gogoship.utility.Originator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

public class SignupOtpActivity extends Originator {

    ConstraintLayout regOtp, phone_code;
    List<String> country_Name_list = new ArrayList<>();
    Spinner spinner_country;
    EditText numOne, numTwo, numThree, numFour, numFive, numSix;
    View view1, view2, view3, view4, view5, view6;
    String str_name = "", str_email = "";
    ConstraintLayout varify_otp;
    EditText edt_mobile, edt_name, edt_email;
    String mob_no = "";
    public static String strPhone_Code = "";
    private String verificationId;
    private FirebaseAuth mAuth;
    private Context context;
    String device_tokens = "", language_key = "";
    SharedPreferences sharedPreferences;
    String lookin_for_id_visivible = "1";
    String jj = "";
    ConstraintLayout back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_otp);
        mAuth = FirebaseAuth.getInstance();
        context = SignupOtpActivity.this;
        regOtp = (ConstraintLayout) findViewById(R.id.card_reg_otp);

        edt_mobile = findViewById(R.id.edt_otp_mob);
        edt_name = findViewById(R.id.edt_name_reg_otp);
        edt_email = findViewById(R.id.edt_email_reg_otp);
        phone_code = findViewById(R.id.phone_code);
        spinner_country = findViewById(R.id.spinner_country);
        varify_otp = findViewById(R.id.cons_varify);
        back = findViewById(R.id.cons_main_toolbar);
        sharedPreferences = this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        language_key = sharedPreferences.getString("language_key", "");
        editemailListener();
        edtmobileListener();
        codenumber();
        initt();
        String[] proviance_array = {"+964", "+91"};
        spinner_country.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_spinner_row, R.id.rowTextView, proviance_array));
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strPhone_Code = spinner_country.getSelectedItem().toString();
                varify_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        str_name = edt_name.getText().toString();
                        str_email = edt_email.getText().toString();
                        mob_no = edt_mobile.getText().toString();
                        String mob_with_code = strPhone_Code + "" + mob_no;
                        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mob_country_code", mob_with_code);
                        editor.putString("mob_without_code", mob_no);
                        editor.putString("country_phonecode", strPhone_Code);
                        editor.apply();
                        Log.e("ggg", "      " + mob_with_code);
                        varifyApi(mob_no,strPhone_Code,mob_with_code);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  Toasty.error(EditProfileActivity.this, "002255555", Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("ggg", "      " + jj);
        //  varifyotpListener();
        phonecodeListener();

        regOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_name = edt_name.getText().toString();
                str_email = edt_email.getText().toString();
                mob_no = edt_mobile.getText().toString();

                if (str_name.equals("")) {
                    Toast toast = Toast.makeText(SignupOtpActivity.this, getString(R.string.please_enter_name), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (str_email.equals("")) {
                    Toast toast = Toast.makeText(SignupOtpActivity.this, getString(R.string.please_enter_mail), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (mob_no.equals("")) {
                    Toast toast = Toast.makeText(SignupOtpActivity.this, getString(R.string.please_varify_phone), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    verifyCodes(v);
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

    private void edtmobileListener() {

        edt_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  edt_email.setFocusable(false);
            }
        });
    }

    private void editemailListener() {

        edt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    str_email = edt_email.getText().toString();
                    chkemailApi();
                } else {
                    str_email = edt_email.getText().toString();
                    // chkemailApi();
                }
            }
        });

    }

    private void chkemailApi() {
        //   CustomProgressbar.showProgressBar(SignupOtpActivity.this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<VarifyEmailModel> call = service.varifyEmail(str_email, language_key);
        call.enqueue(new Callback<VarifyEmailModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<VarifyEmailModel> call, @NonNull retrofit2.Response<VarifyEmailModel> response) {
                //   CustomProgressbar.hideProgressBar();
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            Toast.makeText(SignupOtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupOtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            //  CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignupOtpActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignupOtpActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignupOtpActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignupOtpActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignupOtpActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignupOtpActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignupOtpActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignupOtpActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<VarifyEmailModel> call, Throwable t) {
                //   CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(SignupOtpActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignupOtpActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initt() {

        FirebaseApp.getApps(context);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SignupOtpActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                success(instanceIdResult);
            }

            private void success(InstanceIdResult instanceIdResult) {
                // Get new Instance ID token
                device_tokens = instanceIdResult.getToken();
                Log.e("tokens1", "tok" + device_tokens);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("tokens", "tok" + e.getMessage());

                if (Build.VERSION_CODES.KITKAT > Build.VERSION.SDK_INT) {
                    failure();
                } else {
                    device_tokens = "123456";
                }
            }

            private void failure() {
                initt();
            }
        });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

    }

    private void verifyCodes(View v) {

        String code = "" + numOne.getText().toString() + numTwo.getText().toString() + numThree.getText().toString() + numFour.getText().toString() + numFive.getText().toString() + numSix.getText().toString();
        // Toast.makeText(SignUpActivity.this, code, Toast.LENGTH_SHORT).show();
        if (!code.equals("")) {
            Log.e("bhu", "running phase 1");
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(verificationId, code);

            signInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(this, "Enter the Correct verification Code", Toast.LENGTH_SHORT).show();
        }

    }

    private void varifyApi(String mob_no, String strPhone_Code, String mob_with_code) {
        //   CustomProgressbar.showProgressBar(SignupOtpActivity.this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<VarifyModel> call = service.varifyMobile(mob_no, language_key,strPhone_Code);
        call.enqueue(new Callback<VarifyModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<VarifyModel> call, @NonNull retrofit2.Response<VarifyModel> response) {
                //    CustomProgressbar.hideProgressBar();
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            nextButton(mob_with_code);
                        } else {
                            Toast.makeText(SignupOtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            //   CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(SignupOtpActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(SignupOtpActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(SignupOtpActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(SignupOtpActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(SignupOtpActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(SignupOtpActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(SignupOtpActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(SignupOtpActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<VarifyModel> call, Throwable t) {
                CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(SignupOtpActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(SignupOtpActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void nextButton(String mob_no) {

        Log.e("test_sam",mob_no);
        sendVerificationCode(mob_no);
    }

    private void sendVerificationCode(String mobno) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobno,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            // viewFlipper.setDisplayedChild(1);
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            signInWithPhoneAuthCredential(phoneAuthCredential);
            // checking if the code
            // is null or not.

            //verificationId = s;
            //viewFlipper.setDisplayedChild(1);

            if (code != null) {
                //   Forget_Login();
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                //edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                // verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(SignupOtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            //   Toast.makeText(SignUpActivity.this,"Please enter correct no", Toast.LENGTH_LONG).show();

        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String mob_countrycode = sharedPreferences.getString("mob_without_code", "");
                            String country_code = sharedPreferences.getString("country_phonecode", "");
                            Log.e("chk_m", "       " + mob_countrycode);
                            Intent intent = new Intent(SignupOtpActivity.this, SignupFinalActivity.class);
                            intent.putExtra("str_name", str_name);
                            intent.putExtra("str_Email", str_email);
                            intent.putExtra("str_phone", mob_countrycode);
                            intent.putExtra("country_code_phone", country_code);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "OTP verification successfull", Toast.LENGTH_SHORT).show();
                        } else {
                            // progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Wrong OTP !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void codenumber() {

        numOne = findViewById(R.id.numone);
        numTwo = findViewById(R.id.numtwo);
        numThree = findViewById(R.id.numthree);
        numFour = findViewById(R.id.numfour);
        numFive = findViewById(R.id.numfive);
        numSix = findViewById(R.id.numsix);

        numOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numOne.getText().toString().length() == 0) {
                    numTwo.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        numTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numTwo.getText().toString().length() == 0) {
                    numThree.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numTwo.getText().toString().length() == 0) {
                    numOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numTwo.getText().toString().length() == 0) {
                    numOne.requestFocus();
                }
            }
        });

        numThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numThree.getText().toString().length() == 0) {
                    numFour.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numThree.getText().toString().length() == 0) {
                    numTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numThree.getText().toString().length() == 0) {
                    numTwo.requestFocus();
                }
            }
        });

        numFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numFour.getText().toString().length() == 0) {
                    numFive.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numFour.getText().toString().length() == 0) {
                    numThree.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numFour.getText().toString().length() == 0) {
                    numThree.requestFocus();
                }

            }
        });

        numFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (numFive.getText().toString().length() == 0) {
                    numSix.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (numFive.getText().toString().length() == 0) {
                    numFour.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numFive.getText().toString().length() == 0) {
                    numFour.requestFocus();
                }
            }
        });

        numSix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (numSix.getText().toString().length() == 0) {
                    numFive.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (numSix.getText().toString().length() == 0) {
                    numFive.requestFocus();
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
}