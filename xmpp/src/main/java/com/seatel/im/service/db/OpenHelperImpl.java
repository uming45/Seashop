package com.seatel.im.service.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.seatel.im.model.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by eldorado on 17-5-4.
 *
 * 数据库工具类，实现创建表，修改表
 */
public class OpenHelperImpl extends DaoMaster.OpenHelper {
    private final static String TAG = OpenHelperImpl.class.getSimpleName();

    public OpenHelperImpl(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onCreate(Database db) {
        // super.onCreate(db);
        DaoMaster.createAllTables(db, true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        for (int migrateVersion = oldVersion; migrateVersion <= newVersion; migrateVersion++) {
            upgrade(db, migrateVersion);
        }
    }

    // migrate to version 4:
    //      ALTER TABLE MUC_INFO ADD COLUMN subject TEXT
    private void upgrade(SQLiteDatabase db, int migrateVersion) {
        switch (migrateVersion) {
            case 2:
                db.execSQL("ALTER TABLE MUC_INFO ADD COLUMN subject TEXT");
                break;
            case 3:
                db.execSQL("ALTER TABLE MUC_INFO ADD COLUMN subject TEXT");
                break;
        }

        onCreate(db);
    }
}
