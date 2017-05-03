package com.seatel.im;

import android.content.Context;

import com.seatel.im.conn.ConnectionManager;

/**
 * Created by eldorado on 17-4-27.
 *
 * Init xmpp service, connect&login to xmpp
 */
public class ImModule {
    private final static String TAG = ImModule.class.getSimpleName();
    private static Context sContext;
    private static ConnectionManager sManager;

    public static void init(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        if (sContext == null) {
            throw new RuntimeException("should call init first!");
        }

        return sContext;
    }

    /**
     * export to login
     */
    public static void login(String username, String pwd, String domain, int port) {
        sManager = new ConnectionManager(username, pwd, domain, port);
        sManager.connect();
    }

    public void destroy() {
        sManager.disconnect();
    }
}
