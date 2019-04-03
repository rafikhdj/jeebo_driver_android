package com.app.api;

import com.app.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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

    @FormUrlEncoded
    @POST("session")
    Call<LoginResponse> login(@Field("name") String name, @Field("password") String pwd);


}