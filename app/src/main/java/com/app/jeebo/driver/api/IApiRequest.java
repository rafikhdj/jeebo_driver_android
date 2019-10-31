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
import com.app.jeebo.driver.modules.profile.model.EditProfileReq;
import com.app.jeebo.driver.modules.profile.model.UserResultModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

}