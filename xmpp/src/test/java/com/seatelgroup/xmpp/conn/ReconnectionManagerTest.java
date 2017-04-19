package com.seatelgroup.xmpp.conn;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by eldorado on 17-4-14.
 *
 * 重连测试：
 * 1. 实例化
 * 2. 重连触发
 */
public class ReconnectionManagerTest {

    private AbstractXMPPConnection mConn;
    private ReconnectionManager mSpyManager;

    @Before
    public void setUp() throws Exception {
        // 这里做一些准备工作
        mockConnectionCreationListener();
        // when
        mConn = mock(AbstractXMPPConnection.class);
        publishConnectionCreate(mConn);

        onConnectionCreated();
    }

    @Test
    public void getInstanceForNull() throws Exception {
        AbstractXMPPConnection conn = mock(AbstractXMPPConnection.class);
        ReconnectionManager manager = ReconnectionManager.getInstanceForTest(conn);

        assertNull(manager);
    }

    private void onConnectionCreated() {
        // then 监听到连接创建，实例也被创建了
        // ReconnectionManager created
        // asset conn
        ReconnectionManager instanceFor =  ReconnectionManager.getInstanceForTest(mConn);
        assertNotNull(instanceFor);

        this.mSpyManager = spy(instanceFor);
    }

    // mock一个连接生成响应，仅用于证明思路
    private void mockConnectionCreationListener() {
        // given mock一个出来，执行static块
        ConnectionCreationListener mockCreationListener = mock(ConnectionCreationListener.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                XMPPConnection conn = (XMPPConnection) invocation.getArguments()[0];
                ReconnectionManager.getInstanceFor((AbstractXMPPConnection) conn);
                return null;
            }
        }).when(mockCreationListener).connectionCreated(isA(XMPPConnection.class));
        addConnectionCreationListener(mockCreationListener);
    }

    private final static Set<ConnectionCreationListener> connectionEstablishedListeners =
            new CopyOnWriteArraySet<ConnectionCreationListener>();
    private static void addConnectionCreationListener(ConnectionCreationListener listener) {
        connectionEstablishedListeners.add(listener);
        XMPPConnectionRegistry.addConnectionCreationListener(listener);
    }

    private static void publishConnectionCreate(XMPPConnection connection) {
        for (ConnectionCreationListener listener : connectionEstablishedListeners) {
            listener.connectionCreated(connection);
        }
    }

    @Test
    public void reconnect() throws Exception {
        mSpyManager.connectionClosedOnError(new RuntimeException());

        // then reconnect task run
        // verify reconnect
        mSpyManager.reconnect();
    }

}