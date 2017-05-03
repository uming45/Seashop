package com.seatel.im;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by eldorado on 17-5-3.
 *
 * 需要添加gradle-retrolamda依赖，见build.gradle[app, module]
 */
public class LambdaTest {
    private final static String TAG = LambdaTest.class.getSimpleName();

    public interface ILambda {
        boolean foo(String arg);
    }

    @Test
    public void foo() {
        ILambda lambda = arg -> arg != null;
        assertTrue(lambda.foo("bar"));
    }
}
