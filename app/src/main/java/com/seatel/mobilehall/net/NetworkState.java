package com.seatel.mobilehall.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by eldorado on 17-4-11.
 *
 * 手机客户端网络状态
 */
public class NetworkState implements INetworkState, ConnectionListener {
    private final static String TAG = NetworkState.class.getSimpleName();
    private final Context mContext;
    private boolean mIsNetWorkActive;
    private ConnectivityManager mConnectivityManager;

    public NetworkState(Context context) {
        this.mContext = context;
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    // 监听物理网络
    public void trace() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.mContext.registerReceiver(mNetWorkChangeReceiver, intentFilter);
    }

    public void untrace() {
        this.mContext.unregisterReceiver(mNetWorkChangeReceiver);
    }

    @Override
    public boolean isAvailable() {
        return isGprsOrWifiConnected();
    }

    @Override
    public void publicNetStateEvent(NetStateEvent event) {
        EventBus.getDefault().post(event);
    }

    private void publicNetStateEvent(boolean isConnected) {
        if (mIsNetWorkActive != isConnected) {// 和之前的状态不同
            mIsNetWorkActive = isConnected;

            NetStateEvent event = new NetStateEvent();
            event.setConnected(isConnected);
            event.setChangedTime(System.currentTimeMillis());
            publicNetStateEvent(event);
        }
    }

    /**
     * 网络连接状态，暂时不监听wifi和gprs，只监听xmpp连接
     *
     * @return
     */
    public boolean isGprsOrWifiConnected() {
        NetworkInfo gprs = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isConnectedGprs = gprs != null && gprs.isConnected();
        boolean isConnectedWifi = wifi != null && wifi.isConnected();
        return isConnectedGprs || isConnectedWifi;
    }

    /**
     * 监听网络状态改变的广播
     */
    private BroadcastReceiver mNetWorkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                return;
            }
            final boolean isConnected = isGprsOrWifiConnected();
            publicNetStateEvent(isConnected);
        }
    };

    @Override
    public void connected(XMPPConnection connection) {
        Log.d("event-bus", "connected: ");
        Log.d("event-bus", "connected: " + Thread.currentThread().getName());
        publicNetStateEvent(true);
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.d("event-bus", "authenticated: " + resumed);
        Log.d("event-bus", "authenticated: " + Thread.currentThread().getName());
        publicNetStateEvent(true);
    }

    @Override
    public void connectionClosed() {
        Log.d("event-bus", "connectionClosed: ");
        Log.d("event-bus", "connectionClosed: " + Thread.currentThread().getName());
        publicNetStateEvent(false);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d("event-bus", "connectionClosedOnError: ");
        Log.d("event-bus", "connectionClosedOnError: " + Thread.currentThread().getName());
        publicNetStateEvent(false);
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d("event-bus", "reconnectionSuccessful: ");
        Log.d("event-bus", "reconnectionSuccessful: " + Thread.currentThread().getName());
        publicNetStateEvent(true);
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d("event-bus", "reconnectingIn: ");
        Log.d("event-bus", "reconnectingIn: " + Thread.currentThread().getName());
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d("event-bus", "reconnectionFailed: ");
        Log.d("event-bus", "reconnectionFailed: " + Thread.currentThread().getName());
        publicNetStateEvent(false);
    }
}
