package com.seatelgroup.xmpp.util;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class RegexUtilsTest {
    private String[] ips = new String[] {
            "192.168.1.132",
            "192.168.1.0"
    };

    private String[] not_ips = new String[] {
            "im.seatel.com",
            "256.168.1.132",
            "233.122.3",
            "a.168.1.132"
    };

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void isIpDomain() throws Exception {
        for (String ip : ips) {
            assertTrue(RegexUtils.isIpDomain(ip));
        }

        for (String notIp : not_ips) {
            assertFalse(RegexUtils.isIpDomain(notIp));
        }
    }

}