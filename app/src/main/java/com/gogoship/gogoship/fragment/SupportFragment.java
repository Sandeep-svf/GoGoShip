package com.gogoship.gogoship.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.adapter.CreateTicketAdapter;
import com.gogoship.gogoship.model.OrderNoModel;
import com.gogoship.gogoship.model.SupportListModel;
import com.gogoship.gogoship.model.SupportModel;
import com.gogoship.gogoship.response.OrderNoResponse;
import com.gogoship.gogoship.response.SupportListResponse;
import com.gogoship.gogoship.util.ImagePickerActivity;
import com.gogoship.gogoship.utility.CustomProgressbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SupportFragment extends Fragment {

    ConstraintLayout create_ticket;
    CreateTicketAdapter createTicketAdapter;
    RecyclerView rv_createticket;
    Spinner sp_ticket;
    String lookin_for_id_visivible = "1";
    ImageView ticket_img;
    File photo1 = null;
    ImageView image_logo;
    TextView img_txt;
    ConstraintLayout save;
    EditText edt_mesage, edt_category;
    String message = "", category = "";
    public static final int REQUEST_IMAGE = 100;
    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "", orderNo = "";
    List<OrderNoResponse> orderNoResponseList = new ArrayList<>();
    List<SupportListResponse> supportListResponseList = new ArrayList<>();
    List<String> ordernoList;
    Spinner order_no;
    ConstraintLayout cons_hav_supp_tick;
    ConstraintLayout cons_cancel;
    AlertDialog alertDialog;
    ImageView cancel;
    String strCategory = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.support_fragment, container, false);

        create_ticket = view.findViewById(R.id.cons_create_ticket1);
        rv_createticket = view.findViewById(R.id.recycle_support_ticket);
        sp_ticket = view.findViewById(R.id.sp_create_ticketr);
        cons_hav_supp_tick = view.findViewById(R.id.have_no_support_ticket);
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        language_key = sharedPreferences.getString("language_key", "");
        user_id = sharedPreferences.getString("user_id", "");
        rv_createticket.setHasFixedSize(true);
        supportListApi();
        ticketListener();
        return view;
    }

    private void supportListApi() {
        //   CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<SupportListModel> call = service.supportList(user_id, language_key);
        call.enqueue(new Callback<SupportListModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SupportListModel> call, @NonNull retrofit2.Response<SupportListModel> response) {
                //  CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            // Toast.makeText(getActivity(),"view profile", Toast.LENGTH_SHORT).show();
                            Log.e("chk_size0", "         ");
                            supportListResponseList = response.body().getSupportListResponseList();
                            Log.e("chk_size1", "         " + supportListResponseList.size());
                            if (supportListResponseList.size() > 0) {
                                cons_hav_supp_tick.setVisibility(View.GONE);
                            }
                            createTicketAdapter = new CreateTicketAdapter(getActivity(), supportListResponseList);
                            rv_createticket.setAdapter(createTicketAdapter);
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            cons_hav_supp_tick.setVisibility(View.VISIBLE);
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
                //   CustomProgressbar.hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<SupportListModel> call, Throwable t) {
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

    private void ordernoList() {
        //    CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<OrderNoModel> call = service.ordernoList(user_id);
        call.enqueue(new Callback<OrderNoModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<OrderNoModel> call, @NonNull retrofit2.Response<OrderNoModel> response) {
                //   CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            // Toast.makeText(getActivity(),"view profile", Toast.LENGTH_SHORT).show();
                            orderNoResponseList = response.body().getOrderNoResponseList();
                            ordernoList = new ArrayList<>();

                            for (int i = 0; i < orderNoResponseList.size(); i++) {
                                String name = String.valueOf(orderNoResponseList.get(i).getOrderNumber());
                                ordernoList.add(name);
                            }
                            SupportFragment.AccountSpinnerAdapter accountSpinnerAdapter = new SupportFragment.AccountSpinnerAdapter(getActivity(), R.layout.custom_spinner, ordernoList);
                            accountSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_row);
                            accountSpinnerAdapter.addAll(ordernoList);
                            //  accountSpinnerAdapter.add("Select");
                            if (language_key.equals("en")) {
                                accountSpinnerAdapter.add("Select");
                            } else if (language_key.equals("ar")) {
                                accountSpinnerAdapter.add("يختار");
                            } else {
                                accountSpinnerAdapter.add("Neqandin");
                            }
                            order_no.setAdapter(accountSpinnerAdapter);
                            order_no.setSelection(accountSpinnerAdapter.getCount());
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
            public void onFailure(@NonNull Call<OrderNoModel> call, Throwable t) {
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

    private void ticketListener() {

        create_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new MaterialAlertDialogBuilder(getActivity(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                        .setView(R.layout.custom_alert_dialog).show();
                language_key = sharedPreferences.getString("language_key", "");
                sp_ticket = alertDialog.findViewById(R.id.sp_create_ticketr);
                ticket_img = alertDialog.findViewById(R.id.ticket_img);
                image_logo = alertDialog.findViewById(R.id.img_upload_photo);
                img_txt = alertDialog.findViewById(R.id.img_txt);
                edt_mesage = alertDialog.findViewById(R.id.explain_prob);
                order_no = alertDialog.findViewById(R.id.sp_order_no);
                cons_cancel = alertDialog.findViewById(R.id.cons_cancel);
                cancel = alertDialog.findViewById(R.id.dialog_cancel);
                save = alertDialog.findViewById(R.id.cons_save);
                String[] proviance_array = {"Select", "boy", "girls", "kids", "women", "man", "old man", "fashion", "jeans"};
                sp_ticket.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_row, R.id.rowTextView, proviance_array));
                sp_ticket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!(lookin_for_id_visivible.equals(""))) {                        // et_Merital.setText(sp_loking.getSelectedItem().toString());

                            sp_ticket.setSelection((Integer.parseInt(lookin_for_id_visivible)) - 1);
                            lookin_for_id_visivible = "";
                        } else {
                            strCategory = sp_ticket.getSelectedItem().toString();
                            //  lookin_for_id = String.valueOf(lookng_models_list.get(position).getId());
                        }
                        //Toast.makeText(Add_Profile_Details.this, ""+merital_status_id, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //  Toasty.error(EditProfileActivity.this, "002255555", Toast.LENGTH_SHORT).show();
                    }
                });
                ordernoList();
                List<String> strings = new ArrayList<>();
                // strings.add("Select Account");

                SupportFragment.AccountSpinnerAdapter accountSpinnerAdapter = new SupportFragment.AccountSpinnerAdapter(getActivity(), R.layout.custom_spinner, strings);
                accountSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_row);
                accountSpinnerAdapter.addAll(strings);
                if (language_key.equals("en")) {
                    accountSpinnerAdapter.add("Select");
                } else if (language_key.equals("ar")) {
                    accountSpinnerAdapter.add("يختار");
                } else {
                    accountSpinnerAdapter.add("Neqandin");
                }

                order_no.setAdapter(accountSpinnerAdapter);
                order_no.setSelection(accountSpinnerAdapter.getCount());

                order_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        String item = order_no.getSelectedItem().toString();
                        if (item.equals("Select")) {
                            //  accountSpinnerAdapter.add("Select");
                            // int spinnerPosition = dAdapter.getPosition(compareValue);
                            // spinner_category.setSelection(Integer.parseInt("Select Category"));
                        } else if (item.equals("يختار")) {
                            //  accountSpinnerAdapter.add("يختار");
                            // int spinnerPosition = dAdapter.getPosition(compareValue);
                            // spinner_category.setSelection(Integer.parseInt("Select Category"));
                        } else if (item.equals("Neqandin")) {
                            // accountSpinnerAdapter.add("Neqandin");
                            // int spinnerPosition = dAdapter.getPosition(compareValue);
                            // spinner_category.setSelection(Integer.parseInt("Select Category"));
                        } else {
                            orderNo = String.valueOf(orderNoResponseList.get(position).getOrderNumber());
                            // Log.e("Spinners", "  Name;  " + str_from_where);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                imageListener();
                saveListener();
                cancelListener();
                crossListener();
            }
        });

    }

    private void crossListener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void cancelListener() {
        cons_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void saveListener() {

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = edt_mesage.getText().toString();
                // language_key=sharedPreferences.getString("language_key","");
                saveApi();
            }
        });

    }

    private void saveApi() {
        MultipartBody.Part profileImage = null;
        try {
            try {
                RequestBody requestfaceFile = RequestBody.create(photo1, MediaType.parse("image/*"));
                profileImage = MultipartBody.Part.createFormData("attachment", photo1.getName(), requestfaceFile);

            } catch (Exception e) {
                Log.e("conversionException", "errr" + e.getMessage());
            }
        } catch (Exception e) {
            Log.v("dahgsdhjgdfhjs", "***********************************************" + e);
            //Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

        // CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<SupportModel> call = service.getSupport(user_id, profileImage, strCategory, message, language_key, orderNo);
        call.enqueue(new Callback<SupportModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<SupportModel> call, @NonNull retrofit2.Response<SupportModel> response) {
                //  CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            supportListApi();
                            alertDialog.dismiss();

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        try {
                            CustomProgressbar.hideProgressBar();
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
            public void onFailure(@NonNull Call<SupportModel> call, Throwable t) {
                //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void imageListener() {

        ticket_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedAfterPermission();
            }
        });
    }

    private void proceedAfterPermission() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    cameraIntent();

                } else if (options[item].equals("Choose from Gallery")) {

                    galleryIntent();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }

    private void galleryIntent() {


        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);

    }

    private void cameraIntent() {

        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Uri uri = data.getParcelableExtra("path");
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                // String sel_path = getpath(uri);
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                    // loading profile image from local cache
                    //loadProfile(uri.toString());

                    photo1 = new File(uri.getPath());
                    // Log.e("file ", "path " + photo1.getAbsolutePath());
                    // addImage_file_list_models.add(file);

                    image_logo.setVisibility(View.GONE);
                    img_txt.setVisibility(View.GONE);
                    Glide.with(this).load(photo1)
                            .into(ticket_img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
}
