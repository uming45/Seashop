package com.seatel.im.net;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by eldorado on 17-4-27.
 */
@RunWith(MockitoJUnitRunner.class)
public class NetworkStateTest {

    @Mock
    NetworkState mNetworkState;

    @Before
    public void setUp() {

    }

    @Test
    public void mobileConnectivityUnavailableNotify() {
        // given connection is available

        // when connectivity unavailable

        // then notify connection fail
    }
}