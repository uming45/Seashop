package com.seatelgroup.xmpp.conn;

import com.seatelgroup.xmpp.executor.Executor;

import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;

import java.util.Map;
import java.util.WeakHashMap;

import static com.seatelgroup.xmpp.conn.ReconnectionManager.ReconnectionPolicy.FIXED_DELAY;

/**
 * Created by eldorado on 17-4-12.
 *
 * 重连采用固定时间段重试的策略
 *
 * TODO 添加统计功能
 */
public class ReconnectionManager implements IReconnectionManager {
    private final static String TAG = ReconnectionManager.class.getSimpleName();

    // 1. register reconnection
    static {
        XMPPConnectionRegistry.addConnectionCreationListener(new ConnectionCreationListener() {
            @Override
            public void connectionCreated(XMPPConnection connection) {
                if (connection instanceof AbstractXMPPConnection) {
                    getInstanceFor((AbstractXMPPConnection) connection);
                }
            }
        });
    }

    private static final Map<AbstractXMPPConnection, ReconnectionManager> INSTANCES =
            new WeakHashMap<>();
    private final AbstractXMPPConnection mConn;

    private ReconnectionPolicy mPolicy;
    private int mFixedDelay;

    /**
     * stop sign
     */
    private boolean done;

    // visible for test
    protected final ConnectionListener connectionListener = new AbstractConnectionListener() {

        @Override
        public void connectionClosed() {
            done = true;
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            done = false;
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            done = false;

            reconnect();
        }
    };

    public void connectionClosedOnError(Exception e) {
        connectionListener.connectionClosedOnError(e);
    }

    private ReconnectionManager(AbstractXMPPConnection conn) {
        this.mConn = conn;
        conn.addConnectionListener(connectionListener);
    }

    /**
     * get reconnect manager related to specified connection
     */
    public static synchronized ReconnectionManager getInstanceFor(AbstractXMPPConnection conn) {
        ReconnectionManager reconnManager = INSTANCES.get(conn);
        if (reconnManager == null) {
            reconnManager = new ReconnectionManager(conn);
            INSTANCES.put(conn, reconnManager);
        }

        return reconnManager;
    }

    public static synchronized ReconnectionManager getInstanceForTest(AbstractXMPPConnection conn) {
        return INSTANCES.get(conn);
    }

    @Override
    public void reconnect() {
        Executor.getInstance().execute(new ReconnectionRunnable(mConn, ReconnectionPolicy.FIXED_DELAY));
    }

    @Override
    public void setReconnectPolicy(ReconnectionPolicy policy) {
        this.mPolicy = policy;
    }

    ReconnectionPolicy getPolicy() {
        return this.mPolicy;
    }

    @Override
    public void setFixedDelay(int delay) {
        this.mFixedDelay = delay;
        setReconnectPolicy(FIXED_DELAY);
    }

    /**
     * 重连策略
     */
    public enum ReconnectionPolicy {
        /**
         * 经过固定时间间隔后重连，默认间隔为10s，可以通过{@link ReconnectionManager#setFixedDelay(int)}
         * 控制间隔大小
         */
        FIXED_DELAY;
    }
}
