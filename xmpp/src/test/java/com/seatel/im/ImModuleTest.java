package com.seatel.im;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by eldorado on 17-4-27.
 *
 * 保证上下文传递进来
 */
@RunWith(MockitoJUnitRunner.class)
public class ImModuleTest {

    @Mock
    Context mContext;

    @Before
    public void init() throws Exception {
        ImModule.init(mContext);
    }

    @Test
    public void getContext() throws Exception {
        assertNotNull(ImModule.getContext());
    }
}