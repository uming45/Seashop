package com.seatel.im.conn;

import android.os.Message;

/**
 * Created by eldorado on 17-4-11.
 * <p>
 * 发送消息接口
 */

public interface IMessageHandler {
    /**
     * 发送消息
     *
     * @param msg 消息实体
     * @param jid 接受者jid
     * @return pack id
     */
    String sendMessage(Message msg, String jid);

    /**
     * 发送文本消息
     *
     * @param msg 消息文本
     * @param jid 接受者jid
     * @return pack id
     */
    String sendMessage(String msg, String jid);
}
