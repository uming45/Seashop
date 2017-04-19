package com.seatelgroup.xmpp.conn;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import com.seatelgroup.xmpp.executor.Executor;
import com.seatelgroup.xmpp.util.RegexUtils;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by eldorado on 17-4-10.
 * <p>
 * 连接xmpp
 */
public class ConnectionManager implements IConnectionManager {
    private final static String TAG = "xmpp连接";
    //    public static final String DOMAIN = "im.seatelgroup.com";
    public static final String DOMAIN = "192.168.1.144";
    public static final String SERVICE = "Android";
    public static final int PORT = 5222;
    private AbstractXMPPConnection mConn;

    private ConnectionListener mConnListener;

    /**
     * 注册重连管理器
     */
    static {
        try {
            //Class.forName("org.jivesoftware.smack.ReconnectionManager");
            Class.forName("com.seatelgroup.xmpp.conn.ReconnectionManager");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public ConnectionManager() {
    }

    public ConnectionManager(ConnectionListener listener) {
        this.mConnListener = listener;
    }

    public void doConnect(String username, String password, String domain, int port) throws XmppStringprepException {
        try {
            XMPPTCPConnectionConfiguration config = getConfig(username, password, domain, port);

            mConn = new XMPPTCPConnection(config);
            if (mConnListener == null) {
                mConnListener = DEFAULT_CONN_LISTENER;
            }
            addOnConnectListener(mConnListener);
            setReconnectPolicy();
            mConn.connect();
            mConn.login();
        }
        // WARN: 注意如果连接过程报错，不会重连
        catch (SmackException e) {
            Log.e(TAG, "doConnect -> SmackException ", e);
            SystemClock.sleep(1_000);
            doConnect(username, password, domain, port);
        } catch (IOException e) {
            Log.e(TAG, "doConnect -> IOException ", e);
        } catch (XMPPException e) {
            Log.e(TAG, "doConnect -> XMPPException ", e);
        } catch (InterruptedException e) {
            Log.e(TAG, "doConnect -> InterruptedException ", e);
        }
    }

    public XMPPConnection getXmppConn() {
        return this.mConn;
    }

    public static XMPPTCPConnectionConfiguration getConfigForTest(String username,
        String password,
        String domain,
        int port) throws XmppStringprepException {
        return getConfig(username, password, domain, port);
    }

    @NonNull
    private static XMPPTCPConnectionConfiguration getConfig(
            String username,
            String password,
            String domain,
            int port) throws XmppStringprepException {

        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder()
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setDebuggerEnabled(true)
                .setUsernameAndPassword(username, password)
                .setSendPresence(true);

        builder.setResource("Android");
        builder.setXmppDomain(domain);

        // set ip host
        if (RegexUtils.isIpDomain(domain)) {
            try {
                builder.setHostAddress(InetAddress.getByName(domain));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        builder.setPort(port);

        return builder.build();
    }


    @Override
    public void connect(final String name, final String pwd, final String domain, final int port) {
        Executor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                // stop previous connection
                if (isConnected()) {
                    disconnect();
                }

                try {
                    doConnect(name, pwd, domain, port);
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean isConnected() {
        if (mConn == null) {
            return false;
        }

        return mConn.isConnected();
    }

    @Override
    public void addOnConnectListener(ConnectionListener listener) {
        if (mConn != null) {
            mConn.addConnectionListener(listener);
        }
    }

    @Override
    public void setOnReceiptReceivedListener(ReceiptReceivedListener listener) {
        DeliveryReceiptManager drManager = DeliveryReceiptManager.getInstanceFor(this.mConn);
        drManager.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
        drManager.addReceiptReceivedListener(listener);
    }

    @Override
    public void disconnect() {
        if (mConn != null) {
            mConn.disconnect();
        }
    }

    @Override
    public void setReconnectPolicy() {
        if (mConn == null) {
            throw new RuntimeException("Connection is null!");
        }

        ReconnectionManager reconnManager = ReconnectionManager.getInstanceFor(mConn);
        reconnManager.enableAutomaticReconnection();
        reconnManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
        reconnManager.setFixedDelay(5);

        XMPPConnectionRegistry.addConnectionCreationListener(new ConnectionCreationListener() {
            @Override
            public void connectionCreated(XMPPConnection connection) {
                if (connection instanceof AbstractXMPPConnection) {
                    ReconnectionManager.getInstanceFor((AbstractXMPPConnection) connection);
                }
            }
        });
    }

    /**
     * 连接监听默认实现
     */
    ConnectionListener DEFAULT_CONN_LISTENER = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection) {
            Log.d(TAG, "connected");
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            Log.d(TAG, "authenticated");

        }

        /**
         * 正常关闭连接
         */
        @Override
        public void connectionClosed() {
            Log.d(TAG, "connectionClosed");
            //TODO 释放资源
        }

        /**
         * 意外断开，可能是服务器断开，手机端网络异常等
         * @param e
         */
        @Override
        public void connectionClosedOnError(Exception e) {
            Log.e(TAG, "connectionClosedOnError", e);
        }

        /**
         * 重连成功
         */
        @Override
        public void reconnectionSuccessful() {
            Log.d(TAG, "reconnectionSuccessful");
        }

        /**
         * 执行重连
         * @param seconds
         */
        @Override
        public void reconnectingIn(int seconds) {
            Log.d(TAG, "reconnectingIn");

            // TODO 检查网络是否可用
        }

        /**
         * 重连失败
         * @param e
         */
        @Override
        public void reconnectionFailed(Exception e) {
            Log.d(TAG, "reconnectionFailed");
        }
    };
}
