package com.gogoship.gogoship.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.model.VarifyModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgetPasswordActivity extends AppCompatActivity {

    ConstraintLayout get_otp;
    EditText edt_mob_no;
    String str_mob = "";
    private String verificationId;
    private FirebaseAuth mAuth;
    private Context context;
    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "";
    EditText numOne, numTwo, numThree, numFour, numFive, numSix;
    ImageView back;
    public static String id = "";
    Spinner spinner_country;
    ConstraintLayout cons_varify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mAuth = FirebaseAuth.getInstance();
        context = ForgetPasswordActivity.this;
        get_otp = findViewById(R.id.card_reg_otp_forget);
        edt_mob_no = findViewById(R.id.edt_phone_forget);
        back = findViewById(R.id.img_back);
        cons_varify = findViewById(R.id.card_varify);
        spinner_country = findViewById(R.id.spinner_country_login);
        sharedPreferences = this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        language_key = sharedPreferences.getString("language_key", "");
        user_id = sharedPreferences.getString("user_id", "");
        String[] proviance_array = {"+964", "+91"};
        spinner_country.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_spinner_row, R.id.rowTextView, proviance_array));
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strPhone_Code = spinner_country.getSelectedItem().toString();

                get_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        str_mob = edt_mob_no.getText().toString();
                        String phone_code = strPhone_Code + "" + str_mob;

                        if (str_mob.equals("")) {
                            Toast toast = Toast.makeText(ForgetPasswordActivity.this, getString(R.string.please_enter_email), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            varifyApi(phone_code,str_mob,strPhone_Code);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  Toasty.error(EditProfileActivity.this, "002255555", Toast.LENGTH_SHORT).show();
            }
        });

        backListener();
        codenumber();
        cons_varifyListener();
        //  getotpListener();
    }

    private void cons_varifyListener() {
        cons_varify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = "" + numOne.getText().toString() + numTwo.getText().toString() + numThree.getText().toString() + numFour.getText().toString() + numFive.getText().toString() + numSix.getText().toString();
                // Toast.makeText(SignUpActivity.this, code, Toast.LENGTH_SHORT).show();
                if (!code.equals("")) {
                    Log.e("bhu", "running phase 1");
                    PhoneAuthCredential credential =
                            PhoneAuthProvider.getCredential(verificationId, code);

                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter the Correct verification Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void backListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void varifyApi(String phone_code, String str_mob, String strPhone_Code) {

        //   CustomProgressbar.showProgressBar(ForgetPasswordActivity.this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<VarifyModel> call = service.rest_phone(str_mob, language_key,strPhone_Code);
        call.enqueue(new Callback<VarifyModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<VarifyModel> call, @NonNull retrofit2.Response<VarifyModel> response) {
                //   CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            id = String.valueOf(response.body().getResuerUserIdResponse().getUserId());
                            nextButton(phone_code);
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            //   CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ForgetPasswordActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ForgetPasswordActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ForgetPasswordActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ForgetPasswordActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ForgetPasswordActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ForgetPasswordActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ForgetPasswordActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ForgetPasswordActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                //    CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<VarifyModel> call, Throwable t) {
                //   CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(ForgetPasswordActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ForgetPasswordActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void nextButton(String phone_code) {
        String fbno = str_mob;
        sendVerificationCode(phone_code);
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
            Toast.makeText(ForgetPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            //   Toast.makeText(SignUpActivity.this,"Please enter correct no", Toast.LENGTH_LONG).show();

        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //  SignupUserpi();
                            Intent intent = new Intent(ForgetPasswordActivity.this, ForgetChangePassword.class);
                            intent.putExtra("reset_user_id", id);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "OTP verification successfull", Toast.LENGTH_SHORT).show();

                            //  progressDialog.dismiss();

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
}