package com.gogoship.gogoship.utility;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gogoship.gogoship.ItemClick;
import com.gogoship.gogoship.MainActivity;
import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.adapter.FastCashAdapter;
import com.gogoship.gogoship.adapter.ZenCashAdapter;
import com.gogoship.gogoship.model.AddWalletCashModel;
import com.gogoship.gogoship.model.ZenCashModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class WalletBottomSheet extends BottomSheetDialogFragment implements ItemClick{

    String string;
    ConstraintLayout zen_cash,fast_pay,credit_debit;
    ConstraintLayout checkout;
    Context context;
    RecyclerView fastpay_recycler,zencash_recycler;
    ZenCashAdapter zenCashAdapter;
    FastCashAdapter fastCashAdapter;
    ItemClick itemClick;
    String str_casamt="";
    ImageView zencash_cross,fastpay_cross;
    SharedPreferences sharedPreferences;
    int groupPosition = 0;
    String user_id="",language_key="";
   // ArrayList<ZenCashModel> zenCashList;
    ImageView cancel;
    public static WalletBottomSheet newInstance(String string) {
        WalletBottomSheet f = new WalletBottomSheet();
        Bundle args = new Bundle();
        args.putString("string", string);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_wallet, container, false);
        TextView tv = (TextView) v.findViewById(R.id.text);
        context=v.getContext();
        itemClick=WalletBottomSheet.this;
        //dialog cancel when touches outside (Optional)
        getDialog().setCanceledOnTouchOutside(true);
        sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        language_key=sharedPreferences.getString("language_key","");
        zen_cash=v.findViewById(R.id.cons_zen_cash);
        fast_pay=v.findViewById(R.id.cons_fast_pay);
        credit_debit=v.findViewById(R.id.cons_credit_debit);
       // prepareGroupData();
        creditdebitListener();
        zencashListener();
        fastpayListener();
        return v;
    }

    private void creditdebitListener() {

        credit_debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog =    new MaterialAlertDialogBuilder(getActivity(),R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                        .setView(R.layout.credit_debit_layout).show();
                cancel=alertDialog.findViewById(R.id.img_cancel_credit);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    private void fastpayListener() {

        fast_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog =    new MaterialAlertDialogBuilder(getActivity(),R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                        .setView(R.layout.add_fund_layout).show();

                checkout=alertDialog.findViewById(R.id.cons_check_out);
                fastpay_recycler=alertDialog.findViewById(R.id.fastpay_recycler);
                fastpay_cross=alertDialog.findViewById(R.id.img_cancel_fastpay);
                fastpay_recycler.setHasFixedSize(true);
                ArrayList<ZenCashModel> zenCashList=new ArrayList<>();
                prepareGroupData(zenCashList);
                fastCashAdapter=new FastCashAdapter(context,groupPosition,zenCashList,itemClick);
                fastpay_recycler.setAdapter(fastCashAdapter);
                fastpay_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      alertDialog.dismiss();
                    }
                });
                checkoutListener();
            }
        });

    }

    private void zencashListener() {

        zen_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog =    new MaterialAlertDialogBuilder(getActivity(),R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                        .setView(R.layout.zencash_popup).show();

                checkout=alertDialog.findViewById(R.id.cons_check_out);
                zencash_recycler=alertDialog.findViewById(R.id.zencash_recycler);
                zencash_cross=alertDialog.findViewById(R.id.img_cancel_zencash);
                zencash_recycler.setHasFixedSize(true);
                ArrayList<ZenCashModel> zenCashList=new ArrayList<>();
                prepareGroupData(zenCashList);
                zenCashAdapter=new ZenCashAdapter(context,groupPosition,zenCashList,itemClick);
                zencash_recycler.setAdapter(zenCashAdapter);

                zencash_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                checkoutListener();

            }
        });

    }

    private void prepareGroupData(ArrayList<ZenCashModel> zenCashList) {
        ZenCashModel zenCashModel = new ZenCashModel("$10");
        zenCashList.add(zenCashModel);

        ZenCashModel zenCashModel1 = new ZenCashModel("$20");
        zenCashList.add(zenCashModel1);

        ZenCashModel zenCashModel2 = new ZenCashModel("$30");
        zenCashList.add(zenCashModel2);

        ZenCashModel zenCashModel3 = new ZenCashModel("$40");
        zenCashList.add(zenCashModel3);

        ZenCashModel zenCashModel4 = new ZenCashModel("$50");
        zenCashList.add(zenCashModel4);


        ZenCashModel zenCashModel5 = new ZenCashModel("$100");
        zenCashList.add(zenCashModel5);

        ZenCashModel zenCashModel6 = new ZenCashModel("$200");
        zenCashList.add(zenCashModel6);

        ZenCashModel zenCashModel7 = new ZenCashModel("other");
        zenCashList.add(zenCashModel7);
    }

    private void checkoutListener() {

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addwalletApi();
            }
        });

    }

    private void addwalletApi() {

      //  CustomProgressbar.showProgressBar(context, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<AddWalletCashModel> call = service.addwalletCash(user_id,str_casamt,language_key);
        //  Log.e("asdsax","tyhodcs"+device_token);
      /*  Log.e("strNameOfFarmer", strNameOfFarmer + " / " + newPhoneNumber + " / " + strEmail + " / " + strDistrictIndividual + " / " + strChiefdomIndividual
                + " / " + strSectionIndividual + " / " +
                strTownIndividual + " / " + device_tokens + " / " + device_tokens + " / " + strPassword);
*/
        call.enqueue(new Callback<AddWalletCashModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<AddWalletCashModel> call, @NonNull retrofit2.Response<AddWalletCashModel> response) {
             //   CustomProgressbar.hideProgressBar();
               /* sharedPrefClass.putString(SharedPrefClass.NAME, strNameOfFarmer);
                sharedPrefClass.putString(SharedPrefClass.PHONENO, strPhoneIndividual);
                sharedPrefClass.putString(SharedPrefClass.ADDRESS, strTownIndividual);*/
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            //Toast.makeText(context,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            context.startActivity(intent);

                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        try {
                        //    CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(context, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(context, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(context, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(context, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(context, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(context, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(context, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<AddWalletCashModel> call, Throwable t) {
            //    CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(context, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(context, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void ontemClick(int position) {
        if (position == 0) {
            ExtraAllHistory("0");
        } else if (position == 1) {
            ExtraAllHistory("1");
        } else if (position == 2) {
            ExtraAllHistory("2");
        } else if (position == 3) {
            ExtraAllHistory("3");
        }else if (position == 4) {
            ExtraAllHistory("4");
        }else if (position == 5) {
            ExtraAllHistory("5");
        }else if (position == 6) {
            ExtraAllHistory("6");
        }else if (position == 7) {
            ExtraAllHistory("7");
        }
    }

    private void ExtraAllHistory(String type) {


        if (type.equals("0")) {
            str_casamt="10";
            Toast.makeText(context, str_casamt, Toast.LENGTH_SHORT).show();
        }else if(type.equals("1")){
            str_casamt="20";
            Toast.makeText(context, str_casamt, Toast.LENGTH_SHORT).show();
        }else if(type.equals("2")){
            str_casamt="30";
            Toast.makeText(context, str_casamt, Toast.LENGTH_SHORT).show();
        }else if(type.equals("3")){
            str_casamt="40";
            Toast.makeText(context, str_casamt, Toast.LENGTH_SHORT).show();
        }else if(type.equals("4")){
            str_casamt="50";
            Toast.makeText(context, str_casamt, Toast.LENGTH_SHORT).show();
        }else if(type.equals("5")){
            str_casamt="100";
            Toast.makeText(context, str_casamt, Toast.LENGTH_SHORT).show();
        }else if(type.equals("6")){
            str_casamt="200";
            Toast.makeText(context, str_casamt, Toast.LENGTH_SHORT).show();
        }else if(type.equals("7")){
            str_casamt="other";
            Toast.makeText(context, str_casamt, Toast.LENGTH_SHORT).show();
        }

    }
}
