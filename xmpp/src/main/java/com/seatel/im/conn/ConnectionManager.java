package com.seatel.im.conn;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import com.seatel.im.executor.Executor;
import com.seatel.im.net.NetworkState;
import com.seatel.im.util.RegexUtils;

import org.jivesoftware.smack.AbstractConnectionListener;
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
    public static final int PORT = 5222;
    private String username;
    private String password;
    private String domain;
    private int port;
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

    public ConnectionManager(@NonNull String username,@NonNull String password,@NonNull String domain, int port) {
        this.username = username;
        this.password = password;
        this.domain = domain;
        this.port = port;

        initNetworkListener();
    }

    private void initNetworkListener() {
        // init mobile net listener
    }

    public void setConnectionListener(ConnectionListener listener) {
        this.mConnListener = listener;
    }

    public void doConnect() throws XmppStringprepException {
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
            // TODO IMPROVE this code
            Log.e(TAG, "doConnect -> SmackException ", e);
            SystemClock.sleep(1_000);
            doConnect();
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
    public void connect() {
        if (!NetworkState.isConnectivityAvailable()) {
            return;
        }

        Executor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                // stop previous connection
                if (isConnected()) {
                    disconnect();
                }

                try {
                    doConnect();
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

        // TODO RM this code
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
    ConnectionListener DEFAULT_CONN_LISTENER = new AbstractConnectionListener() {

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            Log.d(TAG, "authenticated");
            // 登陆成功
        }
    };
}
