package com.seatel.im.conn;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.lang.ref.WeakReference;

import static com.seatel.im.conn.ReconnectionManager.ReconnectionPolicy.FIXED_DELAY;

/**
 * Created by eldorado on 17-4-19.
 *
 * 重连的工人线程
 *
 * TODO 单个连接的统计功能
 */

class ReconnectionRunnable implements Runnable {
    private final static String TAG = ReconnectionRunnable.class.getSimpleName();


    /**
     * Holds the current number of reconnection attempts
     */
    private int attempts = 0;
    private ReconnectionManager.ReconnectionPolicy mPolicy;
    private int mFixedDelay;
    private WeakReference<AbstractXMPPConnection> weakRefConn;
    private boolean connecting;
    private boolean done;
    public static final int DEFAULT_DELAY = 1;

    ReconnectionRunnable(AbstractXMPPConnection conn) {
        this.weakRefConn = new WeakReference<>(conn);
    }

    ReconnectionRunnable(AbstractXMPPConnection conn, ReconnectionManager.ReconnectionPolicy policy) {
        setFixedDelay(DEFAULT_DELAY);
        this.weakRefConn = new WeakReference<>(conn);
    }

    void setConn(AbstractXMPPConnection conn) {
        this.weakRefConn = new WeakReference<>(conn);
    }

    public boolean isConnecting() {
        return this.connecting;
    }

    public void setFixedDelay(int fixedDelay) {
        mFixedDelay = fixedDelay;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
    /**
     * Returns the number of seconds until the next reconnection attempt.
     *
     * @return the number of seconds until the next reconnection attempt.
     */
    private int timeDelay() {
        attempts++;

        // Delay variable to be assigned
        int delay;
        switch (mPolicy) {
            case FIXED_DELAY:
                delay = mFixedDelay;
                break;

            default:
                throw new AssertionError("Unknown reconnection policy " + mPolicy);
        }

        return delay;
    }

    @Override
    public void run() {
        final AbstractXMPPConnection xmppConnection = weakRefConn.get();
        if (xmppConnection == null) {
            return;
        }

        if (mPolicy == null) {
            mPolicy = FIXED_DELAY;
        }

        while (isReconnectionPossible(xmppConnection)) {
            int remainingSeconds = timeDelay();
            try {
                if (doReconnect(xmppConnection))
                    break;

                // wait for seconds
                waitForSeconds(xmppConnection, remainingSeconds);
            } catch (Exception e) {
                handleException(e);
            }
        }

        connecting = false;
    }

    // visible for test
    boolean doReconnect(AbstractXMPPConnection xmppConnection) {
        AbstractXMPPConnection connect = null;
        try {
            connect = xmppConnection.connect();

            if (!isAuth(connect)) {
                xmppConnection.login();
            }

            if (isAuth(connect)) {
                /*
                no need to notify reconnect successful here
                for (ConnectionListener listener : xmppConnection.getConnectionListeners()) {
                    listener.reconnectionSuccessful();
                }*/
                attempts = 0;
                connecting = false;
                return true;
            }
        } catch (SmackException | IOException | InterruptedException | XMPPException e) {
            connecting = handleException(e);
            return connecting;
        }

        return false;
    }

    private boolean handleException(Exception e) {
        if (e instanceof SmackException.AlreadyConnectedException) {
            // idle reconnect
            attempts = 0;
            return true;
        } else if (e instanceof SmackException || e instanceof IOException
                || e instanceof XMPPException || e instanceof InterruptedException) {
                /*
                not care reconnect fail
                for (ConnectionListener listener : weakRefConn.get().getConnectionListeners()) {
                    listener.reconnectionFailed(e);
                }*/

            return false;
        }

        return false;
    }

    boolean isAuth(AbstractXMPPConnection conn) {
        return conn != null && conn.isAuthenticated();
    }

    void waitForSeconds(AbstractXMPPConnection xmppConnection, int remainingSeconds) {
        while (isReconnectionPossible(xmppConnection) && remainingSeconds > 0) {
            try {
                Thread.sleep(1000);
                remainingSeconds--;
                    /*
                    not care reconnectingIn
                    for (ConnectionListener listener : xmppConnection.getConnectionListeners()) {
                        listener.reconnectingIn(remainingSeconds);
                    }*/
            } catch (InterruptedException e) {
                //LOGGER.log(Level.FINE, "waiting for reconnection interrupted", e);
                System.out.println(e);
                break;
            }
        }
    }

    private boolean isReconnectionPossible(AbstractXMPPConnection xmppConnection) {
        return !done && !xmppConnection.isConnected();
    }

}
