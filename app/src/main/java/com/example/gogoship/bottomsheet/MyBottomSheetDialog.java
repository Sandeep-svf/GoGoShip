package com.mobapps.gogoship.bottomsheet;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mobapps.gogoship.FilterItemClick;
import com.mobapps.gogoship.R;
import com.mobapps.gogoship.RetrofitApi.APIService;
import com.mobapps.gogoship.RetrofitApi.ApiClient;
import com.mobapps.gogoship.adapter.CountryBottomAdapter;
import com.mobapps.gogoship.fragment.DashboardFragment;
import com.mobapps.gogoship.model.FromWhereModel;
import com.mobapps.gogoship.response.FromWhereResponse;
import com.mobapps.gogoship.utility.CustomProgressbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MyBottomSheetDialog extends BottomSheetDialogFragment implements FilterItemClick {

    String string;
    RecyclerView bottom_recycler;
    CountryBottomAdapter countryBottomAdapter;
    List<FromWhereResponse> fromWhereResponseList = new ArrayList<>();
    FilterItemClick filterItemClick;
    SharedPreferences sharedPreferences;
    ConstraintLayout apply_filter;
    int groupPosition = 0;
    String user_id = "";

    public static MyBottomSheetDialog newInstance(String string) {
        MyBottomSheetDialog f = new MyBottomSheetDialog();
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
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_modal, container, false);
        TextView tv = (TextView) v.findViewById(R.id.text);
        bottom_recycler = v.findViewById(R.id.rec_dash_country_list);
        //dialog cancel when touches outside (Optional)
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        getDialog().setCanceledOnTouchOutside(true);
        filterItemClick = MyBottomSheetDialog.this;
        apply_filter = v.findViewById(R.id.cons_apply_filter);
        fromwhereApi();
        return v;
    }


    private void fromwhereApi() {
        // CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<FromWhereModel> call = service.fromWhere(user_id);
        call.enqueue(new Callback<FromWhereModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<FromWhereModel> call, @NonNull retrofit2.Response<FromWhereModel> response) {
                //  CustomProgressbar.hideProgressBar();
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            // Toast.makeText(getActivity(),"view profile", Toast.LENGTH_SHORT).show();
                            fromWhereResponseList = response.body().getFromWhereResponseList();
                            countryBottomAdapter = new CountryBottomAdapter(getActivity(), fromWhereResponseList, filterItemClick, groupPosition);
                            bottom_recycler.setAdapter(countryBottomAdapter);
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
            public void onFailure(@NonNull Call<FromWhereModel> call, Throwable t) {
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


    @Override
    public void onItemClick(int position, String name) {

        apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment dashboardFragment = new DashboardFragment();
                sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("filter_name", name);
                editor.apply();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, dashboardFragment);
                transaction.commit();
            }
        });
    }
}