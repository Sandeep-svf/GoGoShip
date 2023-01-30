package com.gogoship.gogoship.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.gogoship.gogoship.MainActivity;
import com.gogoship.gogoship.R;
import com.gogoship.gogoship.RetrofitApi.APIService;
import com.gogoship.gogoship.RetrofitApi.ApiClient;
import com.gogoship.gogoship.model.ProfileModel;
import com.gogoship.gogoship.model.UpdateProfileModel;
import com.gogoship.gogoship.response.ProfileDataResponse;
import com.gogoship.gogoship.util.ImagePickerActivity;
import com.gogoship.gogoship.utility.CustomProgressbar;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MyProfileFragment extends Fragment {

    TextView textView;
    ProfileDataResponse profileDataResponse;
    EditText name, address, proviance, phone, email, password, gender;
    String str_name = "", str_add = "", str_phone = "", str_email = "", str_pass = "", str_gender = "", str_proviance = "";
    ConstraintLayout update_profile;
    ImageView select_image;
    public static final int REQUEST_IMAGE = 100;
    File photo1 = null;
    ImageView imageView1;

    SharedPreferences sharedPreferences;
    String user_id = "", language_key = "";
    CircularImageView profile_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_profile_fragment, container, false);

        sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        language_key = sharedPreferences.getString("language_key", "");


        name = view.findViewById(R.id.firstNameEdt);
        address = view.findViewById(R.id.addressEdt);
        proviance = view.findViewById(R.id.provianceEdt);
        phone = view.findViewById(R.id.phoneEdt);
        email = view.findViewById(R.id.emailEdt);
        //  password=view.findViewById(R.id.passwordEdt);
        gender = view.findViewById(R.id.genderEdt);
        update_profile = view.findViewById(R.id.card_update_profile);
        select_image = view.findViewById(R.id.select_update_profile);
        imageView1 = view.findViewById(R.id.profile_images);
        profile_image = view.findViewById(R.id.profile_images);
        selectimageListener();
        updateprofileListener();
        profileApi();

        return view;
    }

    private void selectimageListener() {

        select_image.setOnClickListener(new View.OnClickListener() {
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

    private void updateprofileListener() {

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_name = name.getText().toString();
                str_email = email.getText().toString();
                str_phone = phone.getText().toString();
                str_add = address.getText().toString();
                str_gender = gender.getText().toString();
                if (str_gender.equals("male")) {
                    str_gender = "Male";
                } else if (str_gender.equals("female")) {
                    str_gender = "Female";
                }
                str_proviance = proviance.getText().toString();
                //  str_pass=password.getText().toString();

                updateprofileApi(str_gender);
            }
        });

    }

    private void updateprofileApi(String str_gender1) {

        MultipartBody.Part profileImage = null;

        try {
            try {
                RequestBody requestfaceFile = RequestBody.create(photo1, MediaType.parse("image/*"));
                profileImage = MultipartBody.Part.createFormData("profile_image", photo1.getName(), requestfaceFile);

            } catch (Exception e) {
                Log.e("conversionException", "errr" + e.getMessage());
            }
        } catch (Exception e) {
            Log.v("dahgsdhjgdfhjs", "***********************************************" + e);
            //Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

        //  CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<UpdateProfileModel> call = service.updateProfile(user_id, profileImage, str_name, str_phone, str_email, str_gender1, language_key, str_add, str_proviance);
        call.enqueue(new Callback<UpdateProfileModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<UpdateProfileModel> call, @NonNull retrofit2.Response<UpdateProfileModel> response) {
                // CustomProgressbar.hideProgressBar();

                try {
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        String success = response.body().getSuccess();

                        if (success.equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("login_key", "login_value");
                            startActivity(intent);

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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
            public void onFailure(@NonNull Call<UpdateProfileModel> call, Throwable t) {
                // CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void profileApi() {
        // CustomProgressbar.showProgressBar(getActivity(), false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<ProfileModel> call = service.getProfile(user_id, language_key);
        Log.e("asdsax", "tyhodcs" + user_id);
        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ProfileModel> call, @NonNull retrofit2.Response<ProfileModel> response) {
                //  CustomProgressbar.hideProgressBar();
                try {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                            //  Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            profileDataResponse = response.body().getProfileDataResponse();
                            //  Glide.with(profile_image.getContext()).load(profileDataResponse.getProfileImage()).into(profile_image);
                            Glide.with(profile_image.getContext()).load(profileDataResponse.getProfileImage())/*.placeholder(R.drawable.profilelogo)*/.into(profile_image);
                            name.setText("" + profileDataResponse.getName());
                            address.setText("" + profileDataResponse.getAddress());
                            proviance.setText("" + profileDataResponse.getProvince());
                            phone.setText("" + profileDataResponse.getPhone());
                            email.setText("" + profileDataResponse.getEmail());
                            gender.setText("" + profileDataResponse.getGender());
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<ProfileModel> call, Throwable t) {
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

                    Glide.with(this).load(photo1)
                            .into(imageView1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
