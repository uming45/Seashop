package com.seatelgroup.xmpp.conn;

/**
 * Created by eldorado on 17-4-11.
 *
 * 重连管理器
 * 1. 断网后重连
 * 2. 服务挂掉后重连
 */
public interface IReconnectionManager {
    /**
     * 重连
     */
    void reconnect();

    /**
     * 设置策略
     */
    void setReconnectPolicy(ReconnectionManager.ReconnectionPolicy policy);

    /**
     * 设置重连间隔
     */
    void setFixedDelay(int delay);

}
