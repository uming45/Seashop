package com.seatel.im.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by eldorado on 17-4-10.
 *
 * 线程池
 */
public class Executor {
    private final static String TAG = Executor.class.getSimpleName();

    private ThreadPoolExecutor mPool;
    private static volatile Executor sInstance;

    private Executor() {
        BlockingQueue queue = new LinkedBlockingQueue();
        this.mPool = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, queue);
    }

    public static Executor getInstance() {
        if (sInstance == null) {
            synchronized (Executor.class) {
                if (sInstance == null) {
                    sInstance = new Executor();
                }
            }
        }

        return sInstance;
    }

    public void execute(Runnable task) {
        mPool.execute(task);
    }
}
