package com.seatel.im.service.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.seatel.im.model.MucInfo;
import com.seatel.im.model.MucInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by eldorado on 17-5-3.
 *
 * 真机测试： 测试通过说明greenDao已成功地与Android中的Sqlite3设备交互
 * 后续的测试可以直接使用Mockito做一依赖模拟
 *
 * 遗留问题：
 * 1. 嵌套数据的支持
 * 2. 数据库ddl语句的支持 升级： DevOpenHelper.onUpgrade
 * 3. 对Rx风格的支持
 * 4. 架构的原理
 */
public class DbManagerTest {
    private Context mContext;
    private MucInfoDao mMucInfoDao;

    @Before
    public void setup() {
        mContext = InstrumentationRegistry.getContext();
        DbManager.init(mContext);
        mMucInfoDao = DbManager.getSession().getMucInfoDao();
        insertTestData();
    }

    @Test
    public void init() throws Exception {
        assertNotNull(DbManager.getSession());
    }

    @Test
    public void getSession() throws Exception {
        assertNotNull(mMucInfoDao);
    }

    @Test
    public void saveAndQuery() {
        assertEquals(10, mMucInfoDao.count());
    }

    @Test
    public void query() {
        QueryBuilder<MucInfo> qb = mMucInfoDao.queryBuilder()
                .where(MucInfoDao.Properties.Id.gt(0));
        List<MucInfo> list = qb.build().list();
        assertEquals(10, list.size());
    }

    @Test
    public void update() {
        MucInfo mucInfo5 = mMucInfoDao.queryBuilder()
                .where(MucInfoDao.Properties.Id.eq(5))
                .build()
                .unique();

        assertEquals("5", mucInfo5.getId());

        mucInfo5.setId("19");
        mMucInfoDao.update(mucInfo5);

        String id = mMucInfoDao.queryBuilder()
                .where(MucInfoDao.Properties.Id.eq(5))
                .build()
                .unique()
                .getId();
        assertEquals("19", id);
    }

    @After
    public void tearDown() {
        mMucInfoDao.deleteAll();
        assertTrue(mMucInfoDao.count() == 0);
    }

    private void insertTestData() {
        for (int i = 1; i <= 10; i++) {
            MucInfo mucInfo = new MucInfo();
            mucInfo.setId("" + i);
            mucInfo.setName("name:" + i);
            mucInfo.setUserId("userid:" + i);

            mMucInfoDao.insert(mucInfo);
        }
    }
}