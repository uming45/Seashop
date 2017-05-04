package com.seatel.im.service.db;

import com.seatel.im.model.MucInfo;
import com.seatel.im.model.MucInfoDao;

/**
 * Created by eldorado on 17-5-4.
 */
public class GreendDaoUtils {
    private final static String TAG = GreendDaoUtils.class.getSimpleName();


    static void insertTestData(MucInfoDao mucInfoDao) {
        for (int i = 1; i <= 10; i++) {
            MucInfo mucInfo = new MucInfo();
            mucInfo.setId("" + i);
            mucInfo.setName("name:" + i);
            mucInfo.setUserId("userid:" + i);

            mucInfoDao.insert(mucInfo);
        }
    }
}
