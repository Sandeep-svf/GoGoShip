package com.gogoship.gogoship.activity;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.adapter.WalletTransactionAdapter;
import com.gogoship.gogoship.model.TransacionListModel;
import com.gogoship.gogoship.response.TransactionPurchaseList;
import com.gogoship.gogoship.response.TransactionResponse;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class WalletTransactionActivity extends Fragment {

    RecyclerView rv_trans_detail;
    WalletTransactionAdapter walletTransactionAdapter;
    TransactionResponse transactionResponse;
    List<TransactionResponse> transactionResponseList = new ArrayList<>();
    List<TransactionPurchaseList> transactionPurchaseList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_wallet_transaction, container, false);

        rv_trans_detail = (RecyclerView) view.findViewById(R.id.rec_wallet_transaction);
        rv_trans_detail.setHasFixedSize(true);
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        language_key = sharedPreferences.getString("language_key", "");
        transactionApi();
        return view;
    }

    private void transactionApi() {

        //   CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<TransacionListModel> call = service.transactionList(user_id, language_key);
        call.enqueue(new Callback<TransacionListModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<TransacionListModel> call, @NonNull retrofit2.Response<TransacionListModel> response) {
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            //  Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            transactionResponseList = response.body().getTransactionResponseList();
                            walletTransactionAdapter = new WalletTransactionAdapter(getActivity(), transactionResponseList);
                            rv_trans_detail.setAdapter(walletTransactionAdapter);
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            //    CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<TransacionListModel> call, Throwable t) {
                //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}