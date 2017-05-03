package com.seatel.im.service.http;

import com.seatel.im.model.LoginResult;
import com.seatel.im.model.MucInfo;
import com.seatel.im.model.Result;
import com.seatel.im.model.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by eldorado on 17-5-3.
 *
 * 请求的集合
 */
public interface Request {

    @FormUrlEncoded
    @POST("user/login")
    Call<Result<LoginResult>> login(
            @Field("telephone") String telephone,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("user/get")
    Call<Result<UserInfo>> getUserInfo(
            @Field("access_token") String accessToken);

    @FormUrlEncoded
    @POST("room/list/his")
    Call<Result<List<MucInfo>>> getMucInfos(
            @Field("access_token") String accessToken,
            @Field("pageIndex") int pageIndex,
            @Field("pageSize") int pageSize
    );
}
