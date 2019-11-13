package com.app.jeebo.driver.api;

import com.app.jeebo.driver.model.BaseResponse;
import com.app.jeebo.driver.modules.auth.model.ForgotPassRequest;
import com.app.jeebo.driver.modules.auth.model.LoginRequest;
import com.app.jeebo.driver.modules.auth.model.ResendOtpRequest;
import com.app.jeebo.driver.modules.auth.model.ResultModel;
import com.app.jeebo.driver.modules.auth.model.SignupRequest;
import com.app.jeebo.driver.modules.auth.model.SocialLoginReq;
import com.app.jeebo.driver.modules.auth.model.UserModel;
import com.app.jeebo.driver.modules.auth.model.VerifyOtpResponse;
import com.app.jeebo.driver.modules.home.model.AcceptOrderReq;
import com.app.jeebo.driver.modules.home.model.AcceptOrderResponse;
import com.app.jeebo.driver.modules.home.model.CancelCategoriesResponse;
import com.app.jeebo.driver.modules.home.model.CancelOrderRequest;
import com.app.jeebo.driver.modules.home.model.ChangeDeliveryStageReq;
import com.app.jeebo.driver.modules.home.model.ChangeDriverStatusReq;
import com.app.jeebo.driver.modules.home.model.DriverStatsReq;
import com.app.jeebo.driver.modules.home.model.DriverStatsResponse;
import com.app.jeebo.driver.modules.home.model.OrderDetailResponse;
import com.app.jeebo.driver.modules.home.model.OrderListResponse;
import com.app.jeebo.driver.modules.profile.model.EditProfileReq;
import com.app.jeebo.driver.modules.profile.model.UserResultModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiRequest {


//    @FormUrlEncoded
//    @POST("user/signup/email")
//    Call<BaseResponse<UserResponse>> signUpAPI(@Field(FIELD.NAME) String name, @Field(FIELD.EMAIL) String email, @Field(FIELD.PASSWORD) String password);
//
//
//    @GET("user/logout")
//    Call<BaseResponse<CommonApiResponse>> logout();
//
//    (@Field(FIELD.ROOM_ID) String roomId, @Field(FIELD.ISSUBUNSUB) Boolean isSubUnsub);
//
    interface FIELD {
        String ACCESS_LEVEL = "accessLevel";
        String COUNT = "count";
        String UPDATED = "updated";
        String EMOJI = "emoji";
        String ACCESS_TOKEN = "accessToken";
        String NAME = "name";
        String EMAIL = "email";
        String PASSWORD = "password";
        String FRIEND_ACCPT = "frndAccept";
        String PRIVATE_MSG = "privateMsg";
        String PROF_ACCESS = "isProfileAccess";
        String FRI_ACCESS = "isFriendAccess";
        String PHOTO_ACCESS = "isPhotosAccess";
        String VIDEO_ACCESS = "isVideosAccess";
        String REMOVE_PART_ID = "removeParticipantId";
        String LINK = "Ylink";
        String ISSUBUNSUB = "isSubUnsub";
    }

   /* @FormUrlEncoded
    @POST("session")
    Call<LoginResponse> login(@Field("name") String name, @Field("password") String pwd);*/

    @POST("user/resend_otp")
    Call<ResultModel> resendOtp(@Body ResendOtpRequest resendOtpRequest);

    @POST("user/emaillogin")
    Call<UserModel> login(@Header("Authorization") String authorization,
                          @Body LoginRequest loginRequest);

    @POST("user/facebook_login")
    Call<UserModel> socialLogin(@Body SocialLoginReq loginRequest);

    @POST("user/driver_signup")
    Call<BaseResponse<UserModel>> signup(@Body SignupRequest signupRequest);

    @POST("user/verify_otp")
    Call<VerifyOtpResponse> verifyOtp(@Body ResendOtpRequest resendOtpRequest);

    @Multipart
    @POST("user/upload_file")
    Call<UserModel> getImageURL(@Part MultipartBody.Part file);

    @POST("user/forgot_password")
    Call<ResultModel> forgotPassword(@Body ForgotPassRequest passRequest);

    @POST("user/edit_driver_profile")
    Call<UserResultModel> editProfile(@Body EditProfileReq editProfileReq);

    @GET("user/orders_list")
    Call<OrderListResponse> getOrderList(@Query("order_type") String orderType,@Query("page") String page,
                                         @Query("page_size") String pageSize);

    @POST("user/accept_order")
    Call<AcceptOrderResponse> acceptOrder(@Body AcceptOrderReq acceptOrderReq);

    @POST("user/change_delivery_stage")
    Call<AcceptOrderResponse> changeDeliveryStage(@Body ChangeDeliveryStageReq changeDeliveryStageReq);

    @GET("user/cancel_categories")
    Call<CancelCategoriesResponse> getCancelCategories();

    @GET("user/cancel_subcategories")
    Call<CancelCategoriesResponse> getCancelSubCategories(@Query("cancel_category_id") int cancelCategoryId);

    @GET("user/order_details/{id}")
    Call<OrderDetailResponse> getOrderDetails(@Path("id") String id);

    @POST("user/cancel_delivery")
    Call<AcceptOrderResponse> cancelOrder(@Body CancelOrderRequest cancelOrderRequest);

    @POST("user/change_status")
    Call<AcceptOrderResponse> changeDriverStatus(@Body ChangeDriverStatusReq changeDriverStatusReq);

    @POST("user/statistics")
    Call<DriverStatsResponse> getDriverStats(@Body DriverStatsReq driverStatsReq);

}