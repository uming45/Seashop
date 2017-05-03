package com.seatel.im.conn;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;

/**
 * Created by eldorado on 17-4-11.
 *
 * 连接接口
 */
public interface IConnectionManager {

    /**
     * 连接并登录到服务器
     *
     */
    void connect();

    /**
     * 是否连接
     * @return
     */
    boolean isConnected();

    /**
     * 设置连接状态监听
     * @param listener 监听器
     */
    void addOnConnectListener(ConnectionListener listener);

    /**
     * 设置回执监听。回执送达和已读，参考早期QQ的功能
     * 注意发送失败不属于回执的范围
     *
     * @param listener 监听器
     */
    void setOnReceiptReceivedListener(ReceiptReceivedListener listener);

    /**
     * 断开连接
     */
    void disconnect();

    void setReconnectPolicy();
}
