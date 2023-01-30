package com.gogoship.gogoship.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gogoship.gogoship.MainActivity;
import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.model.ChangePasswordModel;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class ChangePassActivity extends AppCompatActivity {

    EditText edt_old, edt_new, edt_confirm;
    ConstraintLayout update;
    String str_old = "", str_new = "", str_confirm = "";
    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "";
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass_frag);


        edt_old = findViewById(R.id.edt_old_pass);
        edt_new = findViewById(R.id.edt_new_password);
        edt_confirm = findViewById(R.id.edt_confirm_password);
        update = findViewById(R.id.card_update_change);
        back = findViewById(R.id.img_back);
        sharedPreferences = this.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        language_key = sharedPreferences.getString("language_key", "");
        backListener();
        updateListener();

    }

    private void backListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateListener() {

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_old = edt_old.getText().toString();
                str_new = edt_new.getText().toString();
                str_confirm = edt_confirm.getText().toString();

                if (str_old.equals("")) {
                    Toast toast = Toast.makeText(ChangePassActivity.this, getString(R.string.please_enter_old_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (str_new.equals("")) {
                    Toast toast = Toast.makeText(ChangePassActivity.this, getString(R.string.please_enter_new_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (str_confirm.equals("")) {
                    Toast toast = Toast.makeText(ChangePassActivity.this, getString(R.string.please_enter_confirm_password), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    changepassApi();
                }
            }
        });

    }

    private void changepassApi() {
        //   CustomProgressbar.showProgressBar(ChangePassActivity.this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ChangePasswordModel> call = service.changepass(user_id, str_new, str_old, language_key);
        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ChangePasswordModel> call, @NonNull retrofit2.Response<ChangePasswordModel> response) {
                //  CustomProgressbar.hideProgressBar();
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            Toast.makeText(ChangePassActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChangePassActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            //  CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(ChangePassActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(ChangePassActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(ChangePassActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(ChangePassActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(ChangePassActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(ChangePassActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(ChangePassActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(ChangePassActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ChangePasswordModel> call, Throwable t) {
                //   CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(ChangePassActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(ChangePassActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
