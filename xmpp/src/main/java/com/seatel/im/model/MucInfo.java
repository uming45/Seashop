package com.seatel.im.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import lombok.Data;

/**
 * Created by eldorado on 17-5-3.
 *
 * 群聊房间的配置信息
 */
@Data
@Entity
public class MucInfo {
    @Id
    private String id;
    private String jid;
    private String name;
    private String nickname;
    private String userId;
    private int userSize;
    private String desc;
    private int maxUserSize;
    private long createTime;
    private long modifyTime;
    @Generated(hash = 240413196)
    public MucInfo(String id, String jid, String name, String nickname,
            String userId, int userSize, String desc, int maxUserSize,
            long createTime, long modifyTime) {
        this.id = id;
        this.jid = jid;
        this.name = name;
        this.nickname = nickname;
        this.userId = userId;
        this.userSize = userSize;
        this.desc = desc;
        this.maxUserSize = maxUserSize;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }
    @Generated(hash = 1873052311)
    public MucInfo() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getJid() {
        return this.jid;
    }
    public void setJid(String jid) {
        this.jid = jid;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getUserSize() {
        return this.userSize;
    }
    public void setUserSize(int userSize) {
        this.userSize = userSize;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public int getMaxUserSize() {
        return this.maxUserSize;
    }
    public void setMaxUserSize(int maxUserSize) {
        this.maxUserSize = maxUserSize;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getModifyTime() {
        return this.modifyTime;
    }
    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }
}
