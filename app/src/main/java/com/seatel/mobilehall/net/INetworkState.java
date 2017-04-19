package com.seatel.mobilehall.net;

/**
 * Created by eldorado on 17-4-11.
 *
 * 网络连接状态接口
 */
public interface INetworkState {
    /**
     * 网络是否可用
     * @return
     */
    boolean isAvailable();

    /**
     * 发布网络状态
     */
    void publicNetStateEvent(NetStateEvent event);

    public static class NetStateEvent {
        boolean connected;
        long changedTime;

        public boolean isConnected() {
            return connected;
        }

        public void setConnected(boolean connected) {
            this.connected = connected;
        }

        public long getChangedTime() {
            return changedTime;
        }

        public void setChangedTime(long changedTime) {
            this.changedTime = changedTime;
        }

        @Override
        public String toString() {
            return "NetStateEvent{" +
                    "connected=" + connected +
                    ", changedTime=" + changedTime +
                    '}';
        }
    }
}
