package com.seatel.im.service.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.seatel.im.ImModule;

/**
 * Created by eldorado on 17-4-11.
 *
 * 手机客户端网络状态
 */
public class NetworkState {
    private final static String TAG = NetworkState.class.getSimpleName();

    public static boolean isConnectivityAvailable() {
        return isGprsOrWifiConnected();
    }

    /**
     * GPRS或Wifi网络是否可用
     */
    private static boolean isGprsOrWifiConnected() {
        ConnectivityManager manager = (ConnectivityManager)
                ImModule.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
