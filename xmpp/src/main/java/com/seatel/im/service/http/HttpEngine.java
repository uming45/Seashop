package com.seatel.im.service.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eldorado on 17-5-3.
 */
public class HttpEngine {
    private final static String TAG = HttpEngine.class.getSimpleName();
    public static final String BASE_URL = "http://im.seatelgroup.com/interfaceps/";
    private final Retrofit mRetrofit;

    private volatile static HttpEngine sInstance;
    private final Request mRequest;

    private HttpEngine() {
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mRequest = mRetrofit.create(Request.class);
    }

    // visible for test
    protected static HttpEngine getInstance() {
        if (sInstance == null) {
            synchronized (HttpEngine.class) {
                if (sInstance == null) {
                    sInstance = new HttpEngine();
                }
            }
        }
        return new HttpEngine();
    }

    public static Request post() {
        return getInstance().mRequest;
    }
}
