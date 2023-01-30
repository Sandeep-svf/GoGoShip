package com.mobapps.gogoship.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobapps.gogoship.R;
import com.mobapps.gogoship.RetrofitApi.APIService;
import com.mobapps.gogoship.RetrofitApi.ApiClient;
import com.mobapps.gogoship.model.ForgetChangePassModel;
import com.mobapps.gogoship.model.VarifyEmailModel;
import com.mobapps.gogoship.utility.CustomProgressbar;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgetChangePassword extends AppCompatActivity {

    ConstraintLayout cons_update_pass;
    EditText edt_new, edt_confirm;
    String str_new_pass = "", str_confirm_pass = "";
    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "";
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_change_password);

        cons_update_pass = findViewById(R.id.card_update_pass);
        edt_new = findViewById(R.id.edt_new_pass);
        edt_confirm = findViewById(R.id.edt_confirm_pass);
        back = findViewById(R.id.img_back);
        sharedPreferences = this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        // user_id = sharedPreferences.getString("user_id", "");
        user_id = getIntent().getStringExtra("reset_user_id");
        language_key = sharedPreferences.getString("language_key", "");
        backListener();
        updatepassListener();
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updatepassListener() {

        cons_update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_new_pass = edt_new.getText().toString();
                str_confirm_pass = edt_confirm.getText().toString();
                if (str_new_pass.equals("")) {
                    Toast toast = Toast.makeText(ForgetChangePassword.this, getString(R.string.please_enter_new_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (str_confirm_pass.equals("")) {
                    Toast toast = Toast.makeText(ForgetChangePassword.this, getString(R.string.please_enter_confirm_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                   /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("login_key", "login_value");
                    startActivity(intent);*/
                    forgetchangeApi();
                }
            }
        });
    }

    private void forgetchangeApi() {
        //  CustomProgressbar.showProgressBar(ForgetChangePassword.this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ForgetChangePassModel> call = service.forgetchangepass(user_id, str_new_pass, str_new_pass, language_key);
        call.enqueue(new Callback<ForgetChangePassModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ForgetChangePassModel> call, @NonNull retrofit2.Response<ForgetChangePassModel> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            Toast.makeText(ForgetChangePassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgetChangePassword.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ForgetChangePassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            //   CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ForgetChangePassword.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ForgetChangePassword.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ForgetChangePassword.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ForgetChangePassword.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ForgetChangePassword.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ForgetChangePassword.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ForgetChangePassword.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ForgetChangePassword.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ForgetChangePassModel> call, Throwable t) {
                //   CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(ForgetChangePassword.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ForgetChangePassword.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}