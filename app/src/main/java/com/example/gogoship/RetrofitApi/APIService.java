package com.mobapps.gogoship.RetrofitApi;


import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.add_wallet_cash;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.archive;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.change_pass;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.create_support_orderno;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.forget_change_pass;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.from_where;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.homepage;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.login;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.my_order;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.new_order;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.notification;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.profile;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.proviance;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.reset_pass_phone;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.signup;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.support;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.support_ticket_list;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.to_where;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.transaction_list;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.update_profile;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.varify_email;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.varify_phone;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.wallet_balance;
import static com.mobapps.gogoship.RetrofitApi.ServiceUrlList.web_detail;

import com.mobapps.gogoship.model.AddWalletCashModel;
import com.mobapps.gogoship.model.ArchiveModel;
import com.mobapps.gogoship.model.ChangePasswordModel;
import com.mobapps.gogoship.model.ForgetChangePassModel;
import com.mobapps.gogoship.model.FromWhereModel;
import com.mobapps.gogoship.model.HomeFragModel;
import com.mobapps.gogoship.model.LoginModel;
import com.mobapps.gogoship.model.MyOrderModel;
import com.mobapps.gogoship.model.NewOrderModel;
import com.mobapps.gogoship.model.NotificationModel;
import com.mobapps.gogoship.model.OrderNoModel;
import com.mobapps.gogoship.model.ProfileModel;
import com.mobapps.gogoship.model.ProvianceModel;
import com.mobapps.gogoship.model.SignupModel;
import com.mobapps.gogoship.model.SupportListModel;
import com.mobapps.gogoship.model.SupportModel;
import com.mobapps.gogoship.model.ToWhereModel;
import com.mobapps.gogoship.model.TransacionListModel;
import com.mobapps.gogoship.model.UpdateProfileModel;
import com.mobapps.gogoship.model.VarifyEmailModel;
import com.mobapps.gogoship.model.VarifyModel;
import com.mobapps.gogoship.model.WalletBalanceModel;
import com.mobapps.gogoship.model.WebsiteDetailModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {

    @FormUrlEncoded
    @POST(signup)
    Call<SignupModel> getSignup(@Field("name") String name,
                                @Field("phone") String phone,
                                @Field("password") String password,
                                @Field("email") String email,
                                @Field("province") String proviance,
                                @Field("street") String street,
                                @Field("city") String city,
                                @Field("gender") String gender,
                                @Field("language") String language,
                                @Field("country_code") String countryCode
                                );


    @FormUrlEncoded
    @POST(varify_phone)
    Call<VarifyModel> varifyMobile(@Field("phone") String phon,
                                   @Field("language") String language,
                                   @Field("country_code") String country_code
                                   );


    @FormUrlEncoded
    @POST(reset_pass_phone)
    Call<VarifyModel> rest_phone(@Field("phone") String phon,
                                   @Field("language") String language,
                                   @Field("country_code") String country_code
    );


    @FormUrlEncoded
    @POST(varify_email)
    Call<VarifyEmailModel> varifyEmail(@Field("email") String email,
                                       @Field("language") String language
                                       );

    @FormUrlEncoded
    @POST(login)
    Call<LoginModel> getLogin(@Field("phone") String phone,
                              @Field("password") String password,
                              @Field("language") String language,
                              @Field("country_code") String country_code
                              );

    @FormUrlEncoded
    @POST(profile)
    Call<ProfileModel> getProfile(@Field("user_id") String id,
                                  @Field("language") String language

    );


    @Multipart
    @POST(update_profile)
    Call<UpdateProfileModel> updateProfile( @Part("user_id") String id,
                                            @Part MultipartBody.Part image,
                                            @Part("name") String name,
                                            @Part("phone") String phone,
                                            @Part("email") String email,
                                            @Part("gender") String gender,
                                            @Part("language") String language,
                                            @Part("street") String city,
                                            @Part("province") String province
    );


    @FormUrlEncoded
    @POST(proviance)
    Call<ProvianceModel> getProviance(@Field("user_id") String id);


    @FormUrlEncoded
    @POST(homepage)
    Call<HomeFragModel> gethomeFrag(@Field("id") String id,
                                    @Field("country") String country
                                    );


    @FormUrlEncoded
    @POST(my_order)
    Call<MyOrderModel> myorder(@Field("user_id") String id,
                               @Field("language") String language,
                               @Field("orderStatus") String orderStatus);

    @FormUrlEncoded
    @POST(wallet_balance)
    Call<WalletBalanceModel> walletBalance(@Field("user_id") String id,
                                           @Field("language") String language
                                           );

    @FormUrlEncoded
    @POST(transaction_list)
    Call<TransacionListModel> transactionList(@Field("user_id") String id,
                                              @Field("language") String language
                                              );


    @FormUrlEncoded
    @POST(forget_change_pass)
    Call<ForgetChangePassModel> forgetchangepass(@Field("user_id") String id,
                                                 @Field("password") String password,
                                                 @Field("re_password") String re_password,
                                                 @Field("language") String language
                                                 );


    @FormUrlEncoded
    @POST(change_pass)
    Call<ChangePasswordModel> changepass(@Field("user_id") String id,
                                               @Field("password") String password,
                                               @Field("old_password") String old_password,
                                         @Field("language") String language
    );



    @FormUrlEncoded
    @POST(add_wallet_cash)
    Call<AddWalletCashModel> addwalletCash(@Field("user_id") String id,
                                        @Field("amount") String amount,
                                           @Field("language") String language
                                           );


    @Multipart
    @POST(support)
    Call<SupportModel> getSupport(@Part("user_id") String id,
                                  @Part MultipartBody.Part image,
                                  @Part("category") String category,
                                  @Part("message") String message,
                                  @Part("language") String language,
                                  @Part("order_number") String orderNo
                                  );

    @FormUrlEncoded
    @POST(notification)
    Call<NotificationModel> notificationList(@Field("user_id") String id,
                                             @Field("language") String language
                                             );


    @FormUrlEncoded
    @POST(from_where)
    Call<FromWhereModel> fromWhere(@Field("user_id") String id);

    @FormUrlEncoded
    @POST(to_where)
    Call<ToWhereModel> toWhere(@Field("user_id") String id);



    @Multipart
    @POST(new_order)
    Call<NewOrderModel> neworderList(@Part("user_id") String id,
                                     @Part("fromWhere") String from_where,
                                     @Part("toWhere") String to_where,
                                     @Part("shipType") String ship_type,
                                     @Part("insuranceCheck") String insurance_check,
                                     @Part("order_note") String order_note,
                                     @Part("name") String name,
                                     @Part("size") String size,
                                     @Part("color") String color,
                                     @Part("link") String link,
                                     @Part("quantity") String quantity,
                                     @Part MultipartBody.Part image,
                                     @Part("language") String language
    );



    @FormUrlEncoded
    @POST(archive)
    Call<ArchiveModel> archiveList(@Field("user_id") String id,
                                   @Field("language") String language
                                   );

    @FormUrlEncoded
    @POST(create_support_orderno)
    Call<OrderNoModel> ordernoList(@Field("user_id") String id);

    @FormUrlEncoded
    @POST(support_ticket_list)
    Call<SupportListModel> supportList(@Field("user_id") String id,
                                       @Field("language") String language);

    @FormUrlEncoded
    @POST(web_detail)
    Call<WebsiteDetailModel> GetWebDetail(@Field("product_link") String url);

}
