package com.mobapps.gogoship.fragment;

import static android.content.Context.MODE_PRIVATE;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.mobapps.gogoship.MainActivity;
import com.mobapps.gogoship.R;
import com.mobapps.gogoship.RetrofitApi.APIService;
import com.mobapps.gogoship.RetrofitApi.ApiClient;
import com.mobapps.gogoship.activity.LoginActivity;
import com.mobapps.gogoship.activity.LoginSignupActivity;
import com.mobapps.gogoship.activity.SignupFinalActivity;
import com.mobapps.gogoship.adapter.NotificationAdapter;
import com.mobapps.gogoship.bottomsheet.MyBottomSheetDialog;
import com.mobapps.gogoship.bottomsheet.NewOrderBottomSheet;
import com.mobapps.gogoship.model.FromWhereModel;
import com.mobapps.gogoship.model.NewOrderModel;
import com.mobapps.gogoship.model.NotificationModel;
import com.mobapps.gogoship.model.ToWhereModel;
import com.mobapps.gogoship.response.FromWhereResponse;
import com.mobapps.gogoship.response.ToWhereResponse;
import com.mobapps.gogoship.util.ImagePickerActivity;
import com.mobapps.gogoship.utility.CustomProgressbar;
import com.mobapps.gogoship.webview.WebsiteWebviewActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NewOrderFragment extends Fragment {

    boolean visible;
    Spinner spinner_from, spinner_to;
    List<FromWhereResponse> fromWhereResponseList = new ArrayList<>();
    List<ToWhereResponse> toWhereResponseList = new ArrayList<>();
    ConstraintLayout upload;
    Context context;
    TextView from;
    List<String> toList;
    TextView txt_spinner;
    List<String> strings = new ArrayList<>();
    List<String> fromtoList;
    ConstraintLayout buy_now;
    String str_from_where = "", str_to_where = "";
    String str_ship_type = "", str_insurance_check = "", str_order_note = "", str_name = "", str_size = "", str_color = "", str_link = "", str_quantity = "", str_attachment = "";
    EditText edt_order_note, edt_name, edt_size, edt_color, edt_link;
    TextView edt_quantity;
    ImageView ticket_img;
    ConstraintLayout cons_sea, cons_air, cons_land;
    public static final int REQUEST_IMAGE = 100;
    File photo1 = null;
    ImageView image_logo;
    TextView img_txt;
    DashboardFragment dashboardFragment;
    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "";
    int count = 0;
    CheckBox checkBox;
    ImageView img_product, incrementProductQuantityCart, decrementProductQuantityCart;
    BottomSheetDialogFragment bottomSheetDialogFragment;
    String prod_photo = "";
    ConstraintLayout couponCode;
    PopupWindow popupWindow;
    ConstraintLayout keep_shopping, back_home, order_cancel;
    String From = "", To = "";
    boolean processClick = true;
    String prod_name = "", prod_url = "", prod_img = "";
    ImageView prodImg;
    android.app.AlertDialog dialogs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_order_layout, container, false);
        Log.e("tag", "onCreateView");
        spinner_from = view.findViewById(R.id.sp_from);
        upload = view.findViewById(R.id.cons_lay_upload);
        spinner_to = view.findViewById(R.id.sp_to);

        // ticket_img=view.findViewById(R.id.ticket_img);
        image_logo = view.findViewById(R.id.img_upload_photo);
        img_txt = view.findViewById(R.id.img_txt);
        edt_order_note = view.findViewById(R.id.edt_note);
        edt_name = view.findViewById(R.id.edt_product_name);
        edt_size = view.findViewById(R.id.edt_prod_size);
        edt_color = view.findViewById(R.id.edt_product_color);
        edt_link = view.findViewById(R.id.edt_product_url);
        edt_quantity = view.findViewById(R.id.edt_prod_quant);
        cons_sea = view.findViewById(R.id.cons_sea);
        cons_air = view.findViewById(R.id.cons_air);
        cons_land = view.findViewById(R.id.cons_land);
        buy_now = view.findViewById(R.id.cons_buy_now);
        checkBox = view.findViewById(R.id.check_box);
        couponCode = view.findViewById(R.id.cons_coupon_code);
        order_cancel = view.findViewById(R.id.cons_order_cancel);
        prodImg = view.findViewById(R.id.prod_img);
        incrementProductQuantityCart = view.findViewById(R.id.incrementProductQuantityCart);
        decrementProductQuantityCart = view.findViewById(R.id.decrementProductQuantityCart);
        // txt_spinner=view.findViewById(R.id.txt_prompt_from);
        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        language_key = sharedPreferences.getString("language_key", "");
        String key = getArguments().getString("main_act_key");
        Log.e("key_chk", "       " + key);
        image_logo.setVisibility(View.VISIBLE);
        img_txt.setVisibility(View.VISIBLE);
        if (key.equals("main_act_value1")) {
            if (prod_img != null) {
                //  image_logo.setVisibility(View.GONE);
                //   img_txt.setVisibility(View.GONE);
            }
        } else {
            prod_name = getArguments().getString("main_prod_name");
            //  String prod_price=getArguments().getString("main_prod_price");
            prod_url = getArguments().getString("main_prod_url");
            prod_img = getArguments().getString("prod_img");
            image_logo.setVisibility(View.GONE);
            img_txt.setVisibility(View.GONE);
            Log.e("val_chk", "            " + prod_name + "             " + prod_url + "          " + prod_img);
        }

        bottomSheetDialogFragment = NewOrderBottomSheet.newInstance("Bottom Sheet Dialog");
        Log.e("user_id_check", "     " + user_id);
        dashboardFragment = new DashboardFragment();
        fromwhereApi();
        toWhereApi();
        List<String> strings = new ArrayList<>();
        strings.add("Select Account");
        AccountSpinnerAdapter accountSpinnerAdapter = new AccountSpinnerAdapter(getActivity(), R.layout.custom_spinner, strings);
        accountSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_row);
        accountSpinnerAdapter.addAll(strings);
        // accountSpinnerAdapter.add("From");
        if (language_key.equals("en")) {
            accountSpinnerAdapter.add("From");
        } else if (language_key.equals("ar")) {
            accountSpinnerAdapter.add("من");
        } else {
            accountSpinnerAdapter.add("Ji");
        }

        spinner_from.setAdapter(accountSpinnerAdapter);
        spinner_from.setSelection(accountSpinnerAdapter.getCount());

        spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                String item = spinner_from.getSelectedItem().toString();
                if (item.equals("From")) {
                    // int spinnerPosition = dAdapter.getPosition(compareValue);
                    // spinner_category.setSelection(Integer.parseInt("Select Category"));
                    str_from_where = "";
                } else if (item.equals("من")) {
                    str_from_where = "";
                } else if (item.equals("Ji")) {
                    str_from_where = "";
                } else {
                   /* if(fromWhereResponseList!=null){
                        str_from_where = fromWhereResponseList.get(position).getName();
                    }*/
                    try {
                        str_from_where = fromWhereResponseList.get(position).getName();
                    } catch (IndexOutOfBoundsException e) {

                    }

                    //  Log.e("Spinners", "  Name;  " + str_from_where);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                str_from_where = "";
            }
        });

        AccountSpinnerAdapter accountSpinnerAdapter1 = new AccountSpinnerAdapter(getActivity(), R.layout.custom_spinner, strings);
        accountSpinnerAdapter1.setDropDownViewResource(R.layout.simple_spinner_row);
        accountSpinnerAdapter1.addAll(strings);
        // accountSpinnerAdapter1.add("To");
        if (language_key.equals("en")) {
            accountSpinnerAdapter1.add("To");
        } else if (language_key.equals("ar")) {
            accountSpinnerAdapter1.add("إلى");
        } else {
            accountSpinnerAdapter1.add("Ber");
        }
        spinner_to.setAdapter(accountSpinnerAdapter1);
        spinner_to.setSelection(accountSpinnerAdapter1.getCount());

        spinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                String item = spinner_to.getSelectedItem().toString();
                if (item.equals("To")) {
                    str_to_where = "";
                } else if (item.equals("إلى")) {
                    str_to_where = "";
                } else if (item.equals("Ber")) {
                    str_to_where = "";
                } else {
                    try {
                        str_to_where = toWhereResponseList.get(position).getName();
                    } catch (IndexOutOfBoundsException e) {

                    }

                    // Log.e("Spinners", "  Name;  " + str_to_where);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                str_to_where = "";
            }
        });

        incrementProductQuantityCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = Integer.parseInt(String.valueOf(edt_quantity.getText()));
                count++;
                edt_quantity.setText(String.valueOf(count));
            }
        });

        decrementProductQuantityCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count--;
                    edt_quantity.setText(String.valueOf(count));
                    //  interfacrItemClickListener.onDecrementClick(count, mcartListResponseList.get(position),position);

                }
            }
        });

        seaListener();
        airListener();
        landListener();
        imageListener();
        checkboxListener();

        buynowListener();

        if (user_id != null) {
            str_order_note = sharedPreferences.getString("order_note", "");
            str_name = sharedPreferences.getString("prod_name", "");
            str_size = sharedPreferences.getString("prod_size", "");
            str_color = sharedPreferences.getString("prod_color", "");
            str_link = sharedPreferences.getString("prod_link", "");
            str_quantity = sharedPreferences.getString("prod_quantity", "");
            prod_photo = sharedPreferences.getString("prod_photo", "");

            Log.e("prod_name", "    " + str_name);

            edt_order_note.setText(str_order_note);
            edt_name.setText(str_name);
            edt_size.setText(str_size);
            edt_color.setText(str_color);
            edt_link.setText(str_link);
            // edt_quantity.setText(""+str_quantity);
        }
        cancelorderListener();

        edt_order_note.setText("");
        edt_link.setText("");
        edt_color.setText("");
        edt_size.setText("");
        // edt_name.setText("");
        str_ship_type = "";
        edt_name.post(new Runnable() {
            @Override
            public void run() {
                edt_name.getText().clear();
                edt_order_note.setText("");
                edt_link.setText("");
                edt_color.setText("");
                edt_size.setText("");
                // edt_name.setText("");
                str_ship_type = "";

                if (prod_name != null && prod_url != null) {
                    edt_name.setText(prod_name);
                    edt_link.setText(prod_url);
                    Glide.with(prodImg.getContext()).load(prod_img)/*.placeholder(R.drawable.profilelogo)*/.into(prodImg);
                    // image_logo.setVisibility(View.GONE);
                    // img_txt.setVisibility(View.GONE);
                } else {
                    edt_link.setText(prod_url);
                    image_logo.setVisibility(View.VISIBLE);
                    img_txt.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    private void cancelorderListener() {
        order_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment dashboardFragment = new DashboardFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, dashboardFragment);
                transaction.commit();
            }
        });
    }

    private void checkboxListener() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check_flag) {
                if (check_flag) {
                    couponCode.setVisibility(View.VISIBLE);
                } else {
                    couponCode.setVisibility(View.GONE);
                }
            }

        });
    }

    private void landListener() {

        cons_land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_ship_type = "Land";
                cons_sea.setSelected(false);
                cons_land.setSelected(true);
                cons_air.setSelected(false);
            }
        });

    }

    private void airListener() {

        cons_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_ship_type = "Air";
                cons_sea.setSelected(false);
                cons_land.setSelected(false);
                cons_air.setSelected(true);
            }
        });

    }

    private void seaListener() {

        cons_sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_ship_type = "Sea";
                cons_sea.setSelected(true);
                cons_land.setSelected(false);
                cons_air.setSelected(false);
            }
        });

    }

    private void imageListener() {

        prodImg.setOnClickListener(new View.OnClickListener() {
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

    private void buynowListener() {

        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  buy_now.setEnabled(false);
                str_order_note = edt_order_note.getText().toString();
                str_name = edt_name.getText().toString();
                str_size = edt_size.getText().toString();
                str_color = edt_color.getText().toString();
                str_link = edt_link.getText().toString();
                str_quantity = edt_quantity.getText().toString();
                String from = str_from_where;
                String to = str_to_where;
                Log.e("chk_loc", "         " + from + "               " + to);
                if (str_name.equals("")) {
                    Toast.makeText(getActivity(), R.string.please_enter_product_name, Toast.LENGTH_SHORT).show();
                } else if (str_link.equals("")) {
                    Toast.makeText(getActivity(), R.string.please_enter_product_url, Toast.LENGTH_SHORT).show();
                } else if (str_color.equals("")) {
                    Toast.makeText(getActivity(), R.string.please_enter_product_color, Toast.LENGTH_SHORT).show();
                } else if (str_size.equals("")) {
                    Toast.makeText(getActivity(), R.string.please_enter_product_size, Toast.LENGTH_SHORT).show();
                } else if (str_ship_type.equals("")) {
                    Toast.makeText(getActivity(), R.string.please_choose_transport_mode, Toast.LENGTH_SHORT).show();
                } else {
                    buy_now.setEnabled(false);
                    neworderApi(str_order_note, str_name, str_size, str_color, str_link, str_quantity, from, to);
                }

                // openBottomSheet(str_order_note,str_name,str_size,str_color,str_link,str_quantity);
            }
        });

    }

    private void openBottomSheet(String message) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.new_order_bottom_sheet);
        keep_shopping = bottomSheetDialog.findViewById(R.id.cons_keep_shpping);
        back_home = bottomSheetDialog.findViewById(R.id.cons_back_home);
        keep_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // neworderApi(str_order_note,str_name,str_size,str_color,str_link,str_quantity,bottomSheetDialog);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                buy_now.setEnabled(true);
                bottomSheetDialog.dismiss();
                DashboardFragment dashboardFragment = new DashboardFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, dashboardFragment);
                transaction.commit();
            }
        });

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy_now.setEnabled(true);
                bottomSheetDialog.dismiss();
                DashboardFragment dashboardFragment = new DashboardFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container, dashboardFragment);
                transaction.commit();
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    private void neworderApi(String str_order_note1, String str_name1, String str_size1, String str_color1, String str_link1, String str_quantity1, String from, String to) {


        MultipartBody.Part attachment = null;

        try {
            try {

                if (prod_img.equals("")) {
                    RequestBody requestfaceFile = RequestBody.create(photo1, MediaType.parse("image/*"));
                    attachment = MultipartBody.Part.createFormData("attachment", photo1.getName(), requestfaceFile);
                } else {
                    //   RequestBody requestfaceFile = RequestBody.create(prod_img, MediaType.parse("image/*"));
                    attachment = MultipartBody.Part.createFormData("attachment", prod_img);
                }

            } catch (Exception e) {
                Log.e("conversionException", "errr" + e.getMessage());
            }
        } catch (Exception e) {
            Log.v("dahgsdhjgdfhjs", "***********************************************" + e);
            //Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

        //  CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<NewOrderModel> call = service.neworderList(user_id, from, to, str_ship_type, "aa", str_order_note1, str_name1, str_size1, str_color1, str_link1, str_quantity1, attachment, language_key);
        call.enqueue(new Callback<NewOrderModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<NewOrderModel> call, @NonNull retrofit2.Response<NewOrderModel> response) {
                //   CustomProgressbar.hideProgressBar();
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {

                            //  Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            // SharedPreferences preferences = getActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                           /* preferences.edit().remove("order_note").apply();
                            preferences.edit().remove("prod_name").apply();
                            preferences.edit().remove("prod_size").apply();
                            preferences.edit().remove("prod_color").apply();
                            preferences.edit().remove("prod_link").apply();
                            preferences.edit().remove("prod_quantity").apply();
                            preferences.edit().remove("prod_photo").apply();*/
                           /* DashboardFragment dashboardFragment1=new DashboardFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment_container,dashboardFragment1);
                            transaction.commit();*/
                         /*  Intent intent=new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);*/

                            edt_order_note.setText("");
                            edt_link.setText("");
                            edt_color.setText("");
                            edt_quantity.setText("");
                            edt_size.setText("");
                            edt_name.setText("");
                            str_ship_type = "";
                            openBottomSheet(response.body().getMessage());
                        } else {

                            //  Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            buy_now.setEnabled(true);
                            // bottomSheetDialog.dismiss();
                            if (user_id.equals("")) {
                                /*sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("order_note", NewOrderFragment.this.str_order_note);
                                editor.putString("prod_name", NewOrderFragment.this.str_name);
                                editor.putString("prod_size", NewOrderFragment.this.str_size);
                                editor.putString("prod_color", NewOrderFragment.this.str_color);
                                editor.putString("prod_link", NewOrderFragment.this.str_link);
                                editor.putString("prod_quantity", NewOrderFragment.this.str_quantity);
                                editor.putString("prod_photo", String.valueOf(photo1));
                                editor.apply();*/
                                Logout_AlertDialog();

                            }

                        }
                    } else {
                        try {
                            //    CustomProgressbar.hideProgressBar();
                            //   bottomSheetDialog.dismiss();
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
                //  bottomSheetDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<NewOrderModel> call, Throwable t) {
                //     CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Logout_AlertDialog() {
        final LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.logout_dialog, null);
        final TextView tvMessage = alertLayout.findViewById(R.id.textViewMessage);
        final TextView btnYes = alertLayout.findViewById(R.id.btnd_delete);
        final TextView btnNo = alertLayout.findViewById(R.id.btn_cancel);
        btnYes.setText(R.string.ok);
        btnNo.setText(R.string.cancel);
        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity());
        // alert.setTitle(this.getString(R.string.confirm_logout));
        tvMessage.setText(this.getString(R.string.login_signup_dialog));
        alert.setView(alertLayout);
        alert.setCancelable(false);

        btnYes.setOnClickListener(v -> {
            dialogs.dismiss();
            Intent intent = new Intent(getActivity(), LoginSignupActivity.class);
            startActivity(intent);
            //  relative_logout.setEnabled(true);

        });

        btnNo.setOnClickListener(v -> {
            dialogs.dismiss();
            // relative_logout.setEnabled(true);
        });

        dialogs = alert.create();
        dialogs.show();
    }

    private void toWhereApi() {
        //   CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ToWhereModel> call = service.toWhere(user_id);
        call.enqueue(new Callback<ToWhereModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ToWhereModel> call, @NonNull retrofit2.Response<ToWhereModel> response) {
                //    CustomProgressbar.hideProgressBar();
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            //  Toast.makeText(getActivity(),"view profile", Toast.LENGTH_SHORT).show();
                            toWhereResponseList = response.body().getToWhereResponseList();
                            toList = new ArrayList<>();

                            for (int i = 0; i < toWhereResponseList.size(); i++) {
                                String name = toWhereResponseList.get(i).getName();
                                toList.add(name);
                            }

                            AccountSpinnerAdapter accountSpinnerAdapter1 = new AccountSpinnerAdapter(getActivity(), R.layout.custom_spinner, toList);
                            accountSpinnerAdapter1.setDropDownViewResource(R.layout.simple_spinner_row);
                            accountSpinnerAdapter1.addAll(toList);
                            if (language_key.equals("en")) {
                                accountSpinnerAdapter1.add("To");
                            } else if (language_key.equals("ar")) {
                                accountSpinnerAdapter1.add("إلى");
                            } else {
                                accountSpinnerAdapter1.add("Ber");
                            }
                            spinner_to.setAdapter(accountSpinnerAdapter1);
                            spinner_to.setSelection(accountSpinnerAdapter1.getCount());

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
            public void onFailure(@NonNull Call<ToWhereModel> call, Throwable t) {
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


    private void fromwhereApi() {
        //   CustomProgressbar.showProgressBar(getActivity(), false);
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
                            fromtoList = new ArrayList<>();

                            for (int i = 0; i < fromWhereResponseList.size(); i++) {
                                String name = fromWhereResponseList.get(i).getName();
                                fromtoList.add(name);
                            }

                            AccountSpinnerAdapter accountSpinnerAdapter = new AccountSpinnerAdapter(getActivity(), R.layout.custom_spinner, fromtoList);
                            accountSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_row);
                            accountSpinnerAdapter.addAll(fromtoList);
                            if (language_key.equals("en")) {
                                accountSpinnerAdapter.add("From");
                            } else if (language_key.equals("ar")) {
                                accountSpinnerAdapter.add("من");
                            } else {
                                accountSpinnerAdapter.add("Ji");
                            }

                            spinner_from.setAdapter(accountSpinnerAdapter);
                            spinner_from.setSelection(accountSpinnerAdapter.getCount());

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
                //  CustomProgressbar.hideProgressBar();
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
                    Log.e("file ", "path " + photo1.getAbsolutePath());
                    // addImage_file_list_models.add(file);

                    image_logo.setVisibility(View.GONE);
                    img_txt.setVisibility(View.GONE);
                    Glide.with(this).load(photo1)
                            .into(prodImg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("tag", "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.e("tag", "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e("tag", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e("tag", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("tag", "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("tag", "onPause");

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("tag", "onStop");

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.e("tag", "onDestroyView");

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.e("tag", "onDestroy");

        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e("tag", "onDetach");

        super.onDetach();
    }
}
