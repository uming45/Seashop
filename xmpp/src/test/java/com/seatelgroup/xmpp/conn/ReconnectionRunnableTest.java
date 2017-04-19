package com.seatelgroup.xmpp.conn;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by eldorado on 17-4-19.
 */
public class ReconnectionRunnableTest {

    private AbstractXMPPConnection mConn;
    private ReconnectionRunnable mReconnTask;
    private final static int RETRY_TIME = 3;

    @Before
    public void setUp() throws Exception {
        // mock & spy
        mConn = mock(AbstractXMPPConnection.class);
        ReconnectionRunnable r = new ReconnectionRunnable(mConn);
        mReconnTask = spy(r);

        mReconnTask.setFixedDelay(1);
    }

    @Test
    public void run() throws Exception {

    }

    @Test
    public void retryMultipleTime() {
        // given
        when(mReconnTask.doReconnect(mConn))
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);

        // when
        mReconnTask.run();

        // then
        // 等待了2次
        verify(mReconnTask, times(2)).waitForSeconds(isA(AbstractXMPPConnection.class), anyInt());
        // 重试了3次
        assertEquals(3, mReconnTask.getAttempts());
    }

    /******* 一次连接测试 *******/
    @Test
    public void reconnectOnceSuccess() throws Exception {
        // given connect & auth ok
        when(mConn.connect()).thenReturn(mConn);
        when(mReconnTask.isAuth(mConn)).thenReturn(false).thenReturn(true);

        // when
        boolean isConectedOk = mReconnTask.doReconnect(mConn);

        // then connect ok
        verify(mConn).login();
        verify(mReconnTask, times(2)).isAuth(mConn);
        assertTrue(isConectedOk);
    }

    @Test(expected = Exception.class)
    public void throwAlreadyConnect() {
        // given
        when(mReconnTask.doReconnect(mConn)).thenThrow(new SmackException.AlreadyConnectedException());

        // when
        mReconnTask.doReconnect(mConn);

        // then
        assertTrue(mReconnTask.isConnecting());
    }

    @Test(expected = Exception.class)
    public void throwOtherException() {
        // given
        when(mReconnTask.doReconnect(mConn)).thenThrow(new SmackException("close!"));

        // when
        mReconnTask.doReconnect(mConn);

        // then
        assertFalse(mReconnTask.isConnecting());
    }

    @Test(expected = Exception.class)
    public void connectThrowNoResponseException() {
        // given
        SmackException.NoResponseException noResponseException = SmackException.NoResponseException.newWith(mConn, "timeout!");
        when(mReconnTask.doReconnect(mConn)).thenThrow(noResponseException);

        // when
        mReconnTask.doReconnect(mConn);

        // then
        assertFalse(mReconnTask.isConnecting());
    }
}