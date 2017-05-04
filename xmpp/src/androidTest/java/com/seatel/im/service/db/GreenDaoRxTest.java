package com.seatel.im.service.db;

import android.support.test.InstrumentationRegistry;

import com.seatel.im.model.MucInfo;
import com.seatel.im.model.MucInfoDao;

import org.greenrobot.greendao.rx.RxDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by eldorado on 17-5-4.
 *
 * Test greenDao rx
 *
 * @{link <a href="https://github.com/greenrobot/greenDAO/commit/850369f77ea7819f38bfc48b089ee46aad4726f3></a>}
 *
 * 这种方法目前还没有一种可靠的测试方法
 */
public class GreenDaoRxTest {

    private MucInfoDao mMucInfoDao;
    private RxDao<MucInfo, String> mRx;
    private long mCount;

    @Before
    public void setUp() throws Exception {
        DbManager.init(InstrumentationRegistry.getContext());
        mMucInfoDao = DbManager.getSession().getMucInfoDao();
        GreendDaoUtils.insertTestData(mMucInfoDao);
        mRx = mMucInfoDao.rx();
    }

    @Test
    public void count() throws Exception {
        Observable<Long> count = mRx.count();
        count.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                mCount = aLong;
                System.out.println(mCount);
            }
        });

        Thread.sleep(1_000);
        //assertEquals(10, mCount);
    }

    @Test
    public void officialCountTest() throws Exception {
        /*TestSubscriber<User> testSubscriber = new TestSubscriber<>();
        databaseHelper.loadUser().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList(user1, user2))*/
        TestSubscriber<Long> testSubscriber = new TestSubscriber<>();
        Observable<Long> count = mRx.count();

        count.subscribeOn(Schedulers.io())
                .subscribe(testSubscriber);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
//        testSubscriber.assertValue(10L);
    }

    @After
    public void tearDown() {
        mMucInfoDao.deleteAll();
    }
}
