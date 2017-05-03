package com.seatel.im.service;

import com.seatel.im.service.http.HttpEngine;

import org.junit.Test;

import java.util.List;

import retrofit2.*;
import retrofit2.Callback;

import static junit.framework.Assert.*;

/**
 * Created by eldorado on 17-5-3.
 */
public class HttpEngineTest {
    @Test
    public void getInstance() throws Exception {
        assertNotNull(HttpEngine.getInstance());
    }

    @Test
    public void login() throws Exception {
        HttpEngine.post().login("0189404529", "e10adc3949ba59abbe56e057f20f883e").enqueue(new Callback<Result<LoginResult>>() {
            @Override
            public void onResponse(Call<Result<LoginResult>> call, Response<Result<LoginResult>> response) {
                System.out.println(response);
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<Result<LoginResult>> call, Throwable t) {
                System.out.println(t);
            }
        });

        Thread.sleep(2_000);
    }

    @Test
    public void getUserInfo() throws Exception {
        HttpEngine.post()
                .getUserInfo("7e033b4b8d9b487ba9d976bf7ef4a681")
                .enqueue(new Callback<Result<UserInfo>>() {
                    @Override
                    public void onResponse(Call<Result<UserInfo>> call, Response<Result<UserInfo>> response) {
                        System.out.println(response);
                        System.out.println(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<Result<UserInfo>> call, Throwable t) {
                        System.out.println(t);
                    }
                });

        Thread.sleep(2_000);
    }

    /*
    理想的代码风格：
           Http.post()
                .doRequest()
                .onSuccess()
                .onFailure()
     */

    @Test
    public void getMucInfos() throws Exception {
        HttpEngine.post()
                .getMucInfos("fd25539ecab3454ea551d893932d2200", 0, 50)
                .enqueue(new Callback<Result<List<MucInfo>>>() {
                    @Override
                    public void onResponse(Call<Result<List<MucInfo>>> call, Response<Result<List<MucInfo>>> response) {
                        System.out.println(response);
                        System.out.println(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<Result<List<MucInfo>>> call, Throwable t) {
                        System.out.println(t);
                    }
                });

        Thread.sleep(2_000);
    }

}