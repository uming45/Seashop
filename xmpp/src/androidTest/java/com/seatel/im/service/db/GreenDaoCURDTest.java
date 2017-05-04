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
 * 1. 嵌套数据的支持 不是一种好的设计方式，考虑存在多张表当中
 *
 * 2. 数据库ddl语句的支持 升级： DevOpenHelper.onUpgrade
 *    a. 重写DaoMaster.OpenHelper#onCreate & onUpgrade {@link OpenHelperImpl}
 *    b. 修改Entity实体类，对应在OpenHelper手动添加DDL语句
 *    c. 在build.gradle[module]中修改schemaVersion
 *
 * 3. 对Rx风格的支持
 * 4. 架构的原理
 */
public class GreenDaoCURDTest {
    private Context mContext;
    private MucInfoDao mMucInfoDao;

    /*************  append a field to MucInfo  **************
        android.database.sqlite.SQLiteException: table MUC_INFO has no column named SUBJECT (code 1): ,
              while compiling: INSERT INTO "MUC_INFO" (...,"SUBJECT") VALUES (...,?)

        at android.database.sqlite.SQLiteConnection.nativePrepareStatement(Native Method)
        at android.database.sqlite.SQLiteConnection.acquirePreparedStatement(SQLiteConnection.java:896)
        at android.database.sqlite.SQLiteConnection.prepare(SQLiteConnection.java:507)
        at android.database.sqlite.SQLiteSession.prepare(SQLiteSession.java:588)
        at android.database.sqlite.SQLiteProgram.<init>(SQLiteProgram.java:58)
        at android.database.sqlite.SQLiteStatement.<init>(SQLiteStatement.java:31)
        at android.database.sqlite.SQLiteDatabase.compileStatement(SQLiteDatabase.java:1024)

        at org.greenrobot.greendao.database.StandardDatabase.compileStatement(StandardDatabase.java:67)
        at org.greenrobot.greendao.internal.TableStatements.getInsertStatement(TableStatements.java:52)
        at org.greenrobot.greendao.AbstractDao.insert(AbstractDao.java:319)

        at com.seatel.im.service.db.GreenDaoCURDTest.insertTestData(GreenDaoCURDTest.java:113)
        at com.seatel.im.service.db.GreenDaoCURDTest.setup(GreenDaoCURDTest.java:41)
     */

    @Before
    public void setup() {
        mContext = InstrumentationRegistry.getContext();
        DbManager.init(mContext);
        mMucInfoDao = DbManager.getSession().getMucInfoDao();
        mMucInfoDao.deleteAll();
        GreendDaoUtils.insertTestData(mMucInfoDao);
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

        assertEquals(10, mMucInfoDao.count());
    }

    // 数据库更新测试
    @Test
    public void upgrade() {
        // APPENDED FIELD: subject
        MucInfo mucInfo5 = mMucInfoDao.queryBuilder()
                .where(MucInfoDao.Properties.Id.eq(5))
                .build()
                .unique();

        System.out.println(mucInfo5.getSubject());

        mucInfo5.setSubject("subject5");
    }

    @After
    public void tearDown() {
        mMucInfoDao.deleteAll();
        assertTrue(mMucInfoDao.count() == 0);
    }

}