package com.mobapps.gogoship.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mobapps.gogoship.R;
import com.mobapps.gogoship.RetrofitApi.APIService;
import com.mobapps.gogoship.RetrofitApi.ApiClient;
import com.mobapps.gogoship.activity.LoginSignupActivity;
import com.mobapps.gogoship.adapter.MyOrderAdapter;
import com.mobapps.gogoship.model.MyOrderModel;
import com.mobapps.gogoship.model.ProfileModel;
import com.mobapps.gogoship.response.MyOrderDetailList;
import com.mobapps.gogoship.response.MyOrderList;
import com.mobapps.gogoship.response.MyOrderResponse;
import com.mobapps.gogoship.utility.CustomProgressbar;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MyOrderFragment extends Fragment {

    MyOrderAdapter myOrderAdapter;
    RecyclerView myorder_rv;
    MyOrderResponse myOrderResponse;
    List<MyOrderDetailList> myorderList = new ArrayList<>();
    String status="";
    SharedPreferences sharedPreferences;
    String user_id="",language_key="";
    Spinner order_status;
    EditText serach;
    String text_name="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_my_order, container, false);

        myorder_rv=view.findViewById(R.id.recycler_my_order);
        order_status=view.findViewById(R.id.order_status);
        serach=view.findViewById(R.id.myorder_search);
        myorder_rv.setHasFixedSize(true);
        String notification_key=getArguments().getString("notification_key");

        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        language_key=sharedPreferences.getString("language_key","");

        String[] orderstatusList = {"New", "Process","Approved","Purchase","Warehouse","Shipped","Arrived","delivered","Archive","cancel"};
        List<String> strings = new ArrayList<>();
        strings.add("Select Account");

        MyOrderFragment.AccountSpinnerAdapter accountSpinnerAdapter = new MyOrderFragment.AccountSpinnerAdapter(getActivity(), R.layout.custom_spinner, orderstatusList);
        accountSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_row);
        accountSpinnerAdapter.addAll(orderstatusList);
        accountSpinnerAdapter.add("All");
        order_status.setAdapter(accountSpinnerAdapter);
        order_status.setSelection(accountSpinnerAdapter.getCount());

        order_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                String item = "a";
                if (item.equals("All")) {

                    // int spinnerPosition = dAdapter.getPosition(compareValue);
                    // spinner_category.setSelection(Integer.parseInt("Select Category"));
                    //dashboardApi();
                } else {
                    status=order_status.getSelectedItem().toString();
                    Log.e("aaa","         "+status);
                    myorderApi();
                   // Log.e("Spinners", "  Name;  " + country);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        if(notification_key.equals("notification_main")){
            sharedPreferences.edit().remove("order_no").apply();
        }else {

        }


       /* if(notification_key.equals("notification_value")){

        }else {
            sharedPreferences.edit().remove("order_no").apply();
        }*/

        myorderApi();

        MyOrderFragment.this.serach.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
               // myOrderAdapter.scrollToPosition(0);
            }

            public void afterTextChanged(Editable editable) {
                MyOrderFragment.this.filterQuery(editable.toString());
                // List<MyOrderDetailList> filteredModelList = filter(myorderList, editable.toString());
               // myOrderAdapter.setFilter(filteredModelList);

            }
        });
        return view;
    }

    private void filterQuery(String text) {
        ArrayList<MyOrderDetailList> filterdNames = new ArrayList<>();
        for (MyOrderDetailList s : myorderList) {
            if (s.getOrderNumber().toLowerCase().contains(text) || s.getOrderNumber().toUpperCase().contains(text) ) {
                filterdNames.add(s);
            }
        }
       // myOrderAdapter.setFilter(filterdNames);
        if(filterdNames.size()>0){
            myOrderAdapter.setFilter(filterdNames);
        }
    }

    private void myorderApi() {

      //  CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<MyOrderModel> call = service.myorder(user_id,language_key,status);
        call.enqueue(new Callback<MyOrderModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<MyOrderModel> call, @NonNull retrofit2.Response<MyOrderModel> response) {
              //  CustomProgressbar.hideProgressBar();
               /* sharedPrefClass.putString(SharedPrefClass.NAME, strNameOfFarmer);
                sharedPrefClass.putString(SharedPrefClass.PHONENO, strPhoneIndividual);
                sharedPrefClass.putString(SharedPrefClass.ADDRESS, strTownIndividual);*/
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                           // Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            myOrderResponse=response.body().getMyOrderResponse();
                            myorderList=myOrderResponse.getMyOrderDetailLists();

                            if(myorderList.size()>0){
                                Log.e("my_order_list","        "+myorderList.size());
                                myOrderAdapter= new MyOrderAdapter(getActivity(),myorderList);
                                myorder_rv.setAdapter(myOrderAdapter);
                            }else {
                                Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                         //   CustomProgressbar.hideProgressBar();
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
              //  CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<MyOrderModel> call, Throwable t) {
             //   CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class AccountSpinnerAdapter extends ArrayAdapter<String> {
        public AccountSpinnerAdapter(Context context, int textViewResourceId, String[] orderstatusList) {
            super(context, textViewResourceId);
            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }

    private List<MyOrderDetailList> filter(List<MyOrderDetailList> datas, String newText) {
        newText = newText.toLowerCase().trim();

        final List<MyOrderDetailList> filteredModelList = new ArrayList<>();
        for (MyOrderDetailList data : datas) {

            for(MyOrderDetailList m : myorderList){
                text_name=m.getOrderNumber();
                if (text_name.equalsIgnoreCase(newText)) {
                    filteredModelList.add(data);
                }
            }

            // String text = text_name;

        }
        return filteredModelList;
    }

}
