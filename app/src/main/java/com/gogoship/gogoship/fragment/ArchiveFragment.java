package com.gogoship.gogoship.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.adapter.ArchiveAdapter;
import com.gogoship.gogoship.model.ArchiveModel;
import com.gogoship.gogoship.response.ArchiveResponse;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ArchiveFragment extends Fragment {


    ArchiveAdapter archiveAdapter;
    RecyclerView rv_archive;
    List<ArchiveResponse> archiveResponseList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "";
    ImageView archiv_img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_archive, container, false);

        rv_archive = view.findViewById(R.id.recycle_archive);
        archiv_img = view.findViewById(R.id.archiv_img);
        rv_archive.setHasFixedSize(true);
        archiv_img.setColorFilter(archiv_img.getContext().getResources().getColor(R.color.archiv_color), PorterDuff.Mode.SRC_ATOP);
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        language_key = sharedPreferences.getString("language_key", "");

        archiveApi();

        return view;
    }

    private void archiveApi() {

        //   CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ArchiveModel> call = service.archiveList(user_id, language_key);
        call.enqueue(new Callback<ArchiveModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ArchiveModel> call, @NonNull retrofit2.Response<ArchiveModel> response) {
                //    CustomProgressbar.hideProgressBar();
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            archiveResponseList = response.body().getArchiveResponseList();
                            // transactionPurchaseList=transactionResponse.getTransactionPurchaseListList();
                            archiveAdapter = new ArchiveAdapter(getActivity(), archiveResponseList);
                            rv_archive.setAdapter(archiveAdapter);
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
                //    CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<ArchiveModel> call, Throwable t) {
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
}
