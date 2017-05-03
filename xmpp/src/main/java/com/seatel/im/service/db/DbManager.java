package com.seatel.im.service.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.seatel.im.model.DaoMaster;
import com.seatel.im.model.DaoSession;

/**
 * Created by eldorado on 17-5-3.
 *
 * 数据库管理器
 * 1. 定义@Entity
 * 2. make
 */
public class DbManager {
    private final static String TAG = DbManager.class.getSimpleName();
    private static DaoMaster.DevOpenHelper sHelper;
    private static SQLiteDatabase sDatabase;
    private static DaoMaster sDaoMaster;

    public static void init(Context context) {
        sHelper =  new DaoMaster.DevOpenHelper(context, "im-db");
        sDatabase = sHelper.getWritableDatabase();
        sDaoMaster = new DaoMaster(sDatabase);
    }

    public static DaoSession getSession() {
        return sDaoMaster.newSession();
    }
}
