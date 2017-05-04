package com.xunshan.rxhelper.rx;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by eldorado on 17-5-4.
 *
 * 异步操作测试
 */
public class AsyncUnitTest {
    private final static String TAG = AsyncUnitTest.class.getSimpleName();

    private static final List<String> WORDS = Arrays.asList(
            "the",
            "quick",
            "brown",
            "fox",
            "jumped",
            "over",
            "the",
            "lazy",
            "dog"
    );

    @Test
    public void testInSameThread() {
        // given
        List<String> results = new ArrayList<>();
        Observable<String> observable = Observable.from(WORDS)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, index) -> String.format("%2d. %s", index, string));
        // when
        observable.subscribe(results::add);

        // then
        assertNotNull(results);
        assertEquals(9, results.size());
        assertTrue(results.contains(" 4. fox"));
    }

    @Test
    public void testUsingTestSubscriber() {
        // given
        TestSubscriber<String> subscriber = new TestSubscriber<>();

        Observable<String> observable = Observable.from(WORDS)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, index) -> String.format("%2d. %s", index, string));

        // when
        observable.subscribe(subscriber);

        // then
        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        assertThat(subscriber.getOnNextEvents(), hasItem(" 4. fox"));
    }

    @Test
    public void testFailure() {
        // given
        TestSubscriber<String> subscriber = new TestSubscriber<>();
        Exception ex = new RuntimeException("boom");

        Observable<String> observable = Observable.from(WORDS)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, index) -> String.format("%2d. %s", index, string))
                .concatWith(Observable.error(ex));

        // when
        observable.subscribe(subscriber);

        // then
        subscriber.assertError(ex);
        subscriber.assertNotCompleted();
    }

    @Test
    public void testInComputationThread() {
        // given
        TestSubscriber<String> subscriber = new TestSubscriber<>();

        Observable<String> observable = Observable.from(WORDS)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, index) -> String.format("%2d. %s", index, string));

        // when
        observable
                .subscribeOn(Schedulers.computation())
                .subscribe(subscriber);

        // then
        //subscriber.assertCompleted(); // java.lang.AssertionError: Not completed! (0 completions)
        //assertThat(subscriber.getOnNextEvents(), hasItem(" 4. fox"));
    }

    // 这种测试方式不能确定是否通过
    @Test
    public void testUsingBlocking() {
        // given
        Observable<String> observable = Observable.from(WORDS)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, index) -> String.format("%2d. %s", index, string));

        // when
        Iterable<String> results = observable
                .subscribeOn(Schedulers.computation())
                .toBlocking()
                .toIterable();

        // then
        assertThat(results, notNullValue());
        assertThat(results, hasItem(" 4. fox"));
        assertEquals(1, iterableWithSize(results));
    }

    private int iterableWithSize(Iterable<String> results) {
        int count = 0;
        while (results.iterator().hasNext()) {
            count++;
            results.iterator().next();
        }

        return count;
    }

    // awaitility
    @Test
    public void testUsingAwaitility() {
        // given
        TestSubscriber<String> subscriber = new TestSubscriber<>();

        Observable<String> observable = Observable.from(WORDS)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, index) -> String.format("%2d. %s", index, string));

        // when
        observable
                .subscribeOn(Schedulers.computation())
                .subscribe(subscriber);
/*

        await()
                .timeout(2, TimeUnit.SECONDS)
                .until();
*/
    }
}

























