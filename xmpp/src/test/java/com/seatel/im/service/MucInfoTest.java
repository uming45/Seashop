package com.seatel.im.service;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by eldorado on 17-5-3.
 *
 * lombok test
 */
public class MucInfoTest {
    @Test
    public void getUserId() throws Exception {
        MucInfo mucInfo = new MucInfo();
        mucInfo.setUserId("one");
        assertEquals(mucInfo.getUserId(), "one");
    }

    @Test
    public void string() throws Exception {
        MucInfo mucInfo = new MucInfo();
        mucInfo.setUserId("one");
        System.out.println(mucInfo);
    }

}