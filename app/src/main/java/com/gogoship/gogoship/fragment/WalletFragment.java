package com.gogoship.gogoship.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gogoship.gogoship.MainActivity;
import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.activity.WalletTransactionActivity;
import com.gogoship.gogoship.model.WalletBalanceModel;
import com.gogoship.gogoship.response.WalletBalanceResponse;
import com.gogoship.gogoship.utility.WalletBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class WalletFragment extends Fragment {


    ConstraintLayout credit_purchase, background;
    WalletBalanceResponse walletBalanceResponse;
    TextView total_balance, purchase_amt;
    BottomSheetDialogFragment bottomSheetDialogFragment;
    TextView transaction_detail;
    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "";
    CardView card_total;
    Double total_bal = 0.00;
    TextView transaction_text, txtSummary;
    View view1, view2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_wallet_balance, container, false);

        credit_purchase = view.findViewById(R.id.cons_credit_purchase);
        total_balance = view.findViewById(R.id.txt_balnace);
        purchase_amt = view.findViewById(R.id.txt_total_purchase);
        background = view.findViewById(R.id.cons_bg);
        transaction_detail = view.findViewById(R.id.txt_trans_detail);
        view1 = view.findViewById(R.id.view_1);
        view2 = view.findViewById(R.id.view_2);
        txtSummary = view.findViewById(R.id.txt_summary);
        card_total = view.findViewById(R.id.card_wallet_total_balance);
        transaction_text = view.findViewById(R.id.trans_text_label);
        ((MainActivity) getActivity()).bottomNav.show(3, true);
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        language_key = sharedPreferences.getString("language_key", "");

        bottomSheetDialogFragment = WalletBottomSheet.newInstance("Bottom Sheet Dialog");
        creditpurchaseListener();
        transactionDetailListener();
        walletbalanceApi();
        return view;
    }

    private void transactionDetailListener() {

        transaction_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletTransactionActivity walletTransactionActivity = new WalletTransactionActivity();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, walletTransactionActivity);
                transaction.commit();
            }
        });

    }

    private void walletbalanceApi() {

        //  CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<WalletBalanceModel> call = service.walletBalance(user_id, language_key);
        call.enqueue(new Callback<WalletBalanceModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<WalletBalanceModel> call, @NonNull retrofit2.Response<WalletBalanceModel> response) {
                //  CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            walletBalanceResponse = response.body().getWalletBalanceResponse();
                            if (walletBalanceResponse.getTtlBalance() == 0.00) {
                                card_total.setVisibility(View.GONE);
                                background.setVisibility(View.VISIBLE);
                                transaction_detail.setVisibility(View.GONE);
                                transaction_text.setText(R.string.wallet_empty);
                                txtSummary.setText(R.string.wallet_empty_text);
                                transaction_text.setTypeface(Typeface.DEFAULT_BOLD);
                            } else {
                                card_total.setVisibility(View.VISIBLE);
                                total_balance.setText("$" + walletBalanceResponse.getTtlBalance());
                                purchase_amt.setText(R.string.credit_purchase + "" + walletBalanceResponse.getTtlPurchaseAmount());
                                background.setVisibility(View.GONE);
                                view1.setVisibility(View.VISIBLE);
                                view2.setVisibility(View.VISIBLE);
                                transaction_detail.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            //  CustomProgressbar.hideProgressBar();
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
            public void onFailure(@NonNull Call<WalletBalanceModel> call, Throwable t) {
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

    private void creditpurchaseListener() {

        credit_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(getActivity(), WalletTransactionActivity.class);
                startActivity(intent);*/

                bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });
    }
}
