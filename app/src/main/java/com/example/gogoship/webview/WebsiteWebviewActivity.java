package com.mobapps.gogoship.webview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobapps.gogoship.MainActivity;
import com.mobapps.gogoship.R;
import com.mobapps.gogoship.RetrofitApi.APIService;
import com.mobapps.gogoship.RetrofitApi.ApiClient;
import com.mobapps.gogoship.model.WebsiteDetailModel;
import com.mobapps.gogoship.response.WebUrlResponse;
import com.mobapps.gogoship.util.Logger;
import com.mobapps.gogoship.utility.Originator;
import com.mobapps.gogoship.utility.Singleton;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class WebsiteWebviewActivity extends Originator {

    WebView webview_comman;
    String type = "", url = "";
    ImageView back;
    SharedPreferences sharedPreferences;
    String language_key="";
    Locale locale2;
    SharedPreferences sharedPreferencesLanguageName;
    String str_lanuage = "1";
    Singleton singleton = Singleton.getInstance();
    private FragmentActivity context;
    ConstraintLayout fatch_detail;
    WebUrlResponse webUrlResponse;
    public  static String currentUrl;
    TextView new_copyOrder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale("ar");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_webview);

        context = WebsiteWebviewActivity.this;
        back=findViewById(R.id.img_back);
        webview_comman = findViewById(R.id.webview_comman);
        fatch_detail=findViewById(R.id.cons_fatch_order_detail);
        new_copyOrder=findViewById(R.id.new_copy_order);
        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        language_key=sharedPreferences.getString("language_text","");
        webview_comman.setWebViewClient(new WebsiteWebviewActivity.MyBrowser());
        webview_comman.getSettings().setJavaScriptEnabled(true);
        webview_comman.getSettings().setDomStorageEnabled(true);
        webview_comman.getSettings().setLoadsImagesAutomatically(true);
        webview_comman.setVerticalScrollBarEnabled(false);
        webview_comman.getSettings().setBuiltInZoomControls(false);
        webview_comman.getSettings().setSupportZoom(false);
        webview_comman.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview_comman.getSettings().setAllowFileAccess(true);

      //  webview_comman.getSettings().setBuiltInZoomControls(true);
      //  webview_comman.getSettings().setSupportZoom(true);
        url=getIntent().getStringExtra("webview_url");
        type=getIntent().getStringExtra("type");
        webview_comman.loadUrl(url);
        // url = "http://ec2-35-90-177-237.us-west-2.compute.amazonaws.com:8000/en/farsi/aboutus/";
        if(type.equals("n11")){
          new_copyOrder.setText(R.string.copy_order);
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
           // builder.setTitle("App Title");
            builder.setMessage(R.string.n11_not_work_popup);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // Create the AlertDialog object and return it
             builder.show();
           // Toast.makeText(WebsiteWebviewActivity.this, R.string.website_not_support_text, Toast.LENGTH_SHORT).show();
        }else if(type.equals("zara") || type.equals("amazon") || type.equals("shein") || type.equals("mango") || type.equals("hepsiburada") ||  type.equals("aliexpress") || type.equals("lcwaikiki") || type.equals("oysho")){
            new_copyOrder.setText(R.string.copy_order);
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // builder.setTitle("App Title");
            builder.setMessage(R.string.website_not_support_text);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            // Create the AlertDialog object and return it
            builder.show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WebsiteWebviewActivity.this,MainActivity.class);
                intent.putExtra("webview_activity","web_key");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        fatch_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("aliexpress")){
                    Intent intent=new Intent(WebsiteWebviewActivity.this,MainActivity.class);
                    // intent.putExtra("web_key","web_value");
                    intent.putExtra("login_key","web_value");
                    intent.putExtra("prod_url",currentUrl);
                    startActivity(intent);
                }else {
                    fetchdetailApi(currentUrl);
                }
                //fetchdetailApi(currentUrl);
            }
        });
    }

    private void fetchdetailApi(String currentUrl1) {
        //  CustomProgressbar.showProgressBar(LoginActivity.this, false);
        APIService service = ApiClient.getClient().create(APIService.class);
        retrofit2.Call<WebsiteDetailModel> call = service.GetWebDetail(currentUrl1);
        call.enqueue(new Callback<WebsiteDetailModel>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<WebsiteDetailModel> call, @NonNull retrofit2.Response<WebsiteDetailModel> response) {
                //   CustomProgressbar.hideProgressBar();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equals("true")) {
                           // Toast.makeText(WebsiteWebviewActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("tag","pass"+currentUrl1);
                            webUrlResponse=response.body().getWebUrlResponse();
                            String product_name=webUrlResponse.getProductName();
                            String product_url=url;
                            String product_price=webUrlResponse.getProductPrice();
                            String productImage=webUrlResponse.getProductImage();
                            Intent intent=new Intent(WebsiteWebviewActivity.this,MainActivity.class);
                            // intent.putExtra("web_key","web_value");
                            intent.putExtra("login_key","web_value");
                            intent.putExtra("prod_name",product_name);
                            intent.putExtra("prod_url",currentUrl1);
                            intent.putExtra("prod_price",product_price);
                            intent.putExtra("prod_img",productImage);
                            startActivity(intent);
                        } else {
                            Log.e("tag","fail"+currentUrl1);
                            Intent intent=new Intent(WebsiteWebviewActivity.this,MainActivity.class);
                            // intent.putExtra("web_key","web_value");
                            intent.putExtra("login_key","web_value");
                            intent.putExtra("prod_url",currentUrl1);
                            startActivity(intent);
                        }
                    } else {
                        try {
                            //  CustomProgressbar.hideProgressBar();
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(WebsiteWebviewActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(WebsiteWebviewActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(WebsiteWebviewActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(WebsiteWebviewActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(WebsiteWebviewActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(WebsiteWebviewActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(WebsiteWebviewActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(WebsiteWebviewActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(@NonNull Call<WebsiteDetailModel> call, Throwable t) {
                //  CustomProgressbar.hideProgressBar();
                if (t instanceof IOException) {
                    Toast.makeText(WebsiteWebviewActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(WebsiteWebviewActivity.this, "Please Check your Internet Connection....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
              currentUrl=request.getUrl().toString();
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url1, Bitmap favicon) {
          //  currentUrl=url1;
            Log.e("chk_str","         "+currentUrl);
          /*  String result = currentUrl.split(".html")[0];
            String result1=result+".html";
            Log.e("chk_str","         "+result1);*/
            super.onPageStarted(view, url, favicon);
            if (!((FragmentActivity) context).isFinishing()) {
              /*  if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }*/
                //   CustomProgressbar.showProgressBar(context, false);
            }

            Logger.line(Logger.LoggerMessage.product_image_url, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "View_On_Website_Activity",
                    url);
        }

        @Override
        public void onPageFinished(WebView view, String url12) {
            // TODO hide your progress image
            super.onPageFinished(view, url12);

            if (!((FragmentActivity) context).isFinishing()) {
               /* if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                //  CustomProgressbar.hideProgressBar();
                Log.e("chk_str12","         "+url12);
                currentUrl=url12;
            }

            Logger.analyser(context, Logger.LoggerMessage.PAYMENT_ACTIVITY, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    url
            );

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (!((FragmentActivity) context).isFinishing()) {
               /* if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                //    CustomProgressbar.hideProgressBar();
            }
            webview_comman.loadUrl(currentUrl);
            Logger.line(Logger.LoggerMessage.product_image_url, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "View_On_Website_Activity",
                    request.getUrl());
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            String urldata=view.getUrl();
            currentUrl=urldata;
            Log.e("print_url","             "+urldata);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(WebsiteWebviewActivity.this,MainActivity.class);
        intent.putExtra("webview_activity","web_key");
        setResult(RESULT_OK,intent);
        finish();
        super.onBackPressed();
    }

}