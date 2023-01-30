package com.mobapps.gogoship.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobapps.gogoship.FilterItemClick;
import com.mobapps.gogoship.MainActivity;
import com.mobapps.gogoship.R;

import com.mobapps.gogoship.RetrofitApi.APIService;
import com.mobapps.gogoship.RetrofitApi.ApiClient;
import com.mobapps.gogoship.adapter.ParentRecyclerViewAdapter;
import com.mobapps.gogoship.bottomsheet.MyBottomSheetDialog;
import com.mobapps.gogoship.model.FromWhereModel;
import com.mobapps.gogoship.model.HomeFragModel;
import com.mobapps.gogoship.response.FromWhereResponse;
import com.mobapps.gogoship.response.HomeFragParentList;
import com.mobapps.gogoship.utility.CustomProgressbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class DashboardFragment extends Fragment implements FilterItemClick {

    private RecyclerView parentRecyclerView;
    private RecyclerView.Adapter ParentAdapter;
    List<HomeFragParentList> homeFragParentLists = new ArrayList<>();
    List<FromWhereResponse> fromWhereResponseList = new ArrayList<>();
    List<String> fromtoList;
    private RecyclerView.LayoutManager parentLayoutManager;
    ConstraintLayout sort;
    BottomSheetDialogFragment bottomSheetDialogFragment;
    SharedPreferences sharedPreferences;
    String user_id="",language_key="";
    Spinner country_list;
    FilterItemClick filterItemClick;
    String country="";
    TextView filter_text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        filterItemClick=DashboardFragment.this;
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        country = sharedPreferences.getString("filter_name", "");
        language_key=sharedPreferences.getString("language_key","");
        Log.e("key_value","    "+language_key);
        bottomSheetDialogFragment = MyBottomSheetDialog.newInstance("Bottom Sheet Dialog");
        sort = view.findViewById(R.id.cons_sortby);
      //  country_list=view.findViewById(R.id.country_list);
        filter_text=view.findViewById(R.id.filter_item_text);
        //  parentModelArrayList.add(new ParentModel("Category1"));
        //  parentModelArrayList.add(new ParentModel("Category2"));

        parentRecyclerView = view.findViewById(R.id.Parent_recyclerView);
        parentRecyclerView.setHasFixedSize(true);
        parentLayoutManager = new LinearLayoutManager(getActivity());
        parentRecyclerView.setLayoutManager(parentLayoutManager);
        ((MainActivity)getActivity()).bottomNav.show(1,true);
        if(country.equals("")){
            filter_text.setText(R.string.all);
        }else {
            filter_text.setText(country);
        }
        fromwhereApi();
        dashboardApi();
        sortListener();

        return view;
    }

    private void fromwhereApi() {

      //  CustomProgressbar.showProgressBar(getActivity(), false);

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<FromWhereModel> call = service.fromWhere(user_id);
        call.enqueue(new Callback<FromWhereModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<FromWhereModel> call, @NonNull retrofit2.Response<FromWhereModel> response) {
            //    CustomProgressbar.hideProgressBar();
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                           // Toast.makeText(getActivity(),"view profile", Toast.LENGTH_SHORT).show();
                            fromWhereResponseList=response.body().getFromWhereResponseList();
                            fromtoList = new ArrayList<>();
                            for(int i =0 ;i<fromWhereResponseList.size();i++){
                                String name=fromWhereResponseList.get(i).getName();
                                fromtoList.add(name);
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
            public void onFailure(@NonNull Call<FromWhereModel> call, Throwable t) {
            //    CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void dashboardApi() {

        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<HomeFragModel> call = service.gethomeFrag(user_id,country);
        call.enqueue(new Callback<HomeFragModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<HomeFragModel> call, @NonNull retrofit2.Response<HomeFragModel> response) {
                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success=response.body().getSuccess();
                        homeFragParentLists=response.body().getHomeFragParentListList();
                        if (success.equalsIgnoreCase("true")) {
                              Log.e("success_check",""+country);
                            //  Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            ParentAdapter = new ParentRecyclerViewAdapter((ArrayList<HomeFragParentList>) homeFragParentLists, getActivity());
                            parentRecyclerView.setAdapter(ParentAdapter);
                            // sendVerificationCode(newPhoneNumber);

                        } else {
                            //Log.e("success_check",""+message);
                        }

                    } else {
                        try {

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
            }

            @Override
            public void onFailure(@NonNull Call<HomeFragModel> call, Throwable t) {

                if (t instanceof IOException) {
                    Toast.makeText(getActivity(),"this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(),"this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void sortListener() {

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogFragment.show(getChildFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }

    @Override
    public void onItemClick(int position, String name) {
    }

    public class AccountSpinnerAdapter extends ArrayAdapter<String> {
        public AccountSpinnerAdapter(Context context, int textViewResourceId, List<String> strings) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        preferences.edit().remove("filter_name").apply();
    }
}
