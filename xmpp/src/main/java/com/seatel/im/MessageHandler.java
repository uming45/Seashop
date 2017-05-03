package com.seatel.im;

import android.os.Message;

import com.seatel.im.conn.IMessageHandler;

/**
 * Created by eldorado on 17-4-11.
 */
public class MessageHandler implements IMessageHandler {
    private final static String TAG = MessageHandler.class.getSimpleName();

    /*
    // send msg code sample
    // here should define your own exception type
    try {
        EntityBareJid entityBareJid = JidCreate.entityBareFrom(jid);
        Chat chat = this.mCm.chatWith(entityBareJid);
        chat.send(msg);
    } catch (XmppStringprepException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (SmackException.NotConnectedException e) {
        e.printStackTrace();
    }*/

    @Override
    public String sendMessage(Message msg, String jid) {
        return null;
    }

    @Override
    public String sendMessage(String msg, String jid) {
        return null;
    }
}
