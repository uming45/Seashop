package com.seatel.im.service;

/**
 * Created by eldorado on 17-5-3.
 */
public class UserInfo {
    private final static String TAG = UserInfo.class.getSimpleName();

    private String userId;
    private int userType;
    private long birthday;
    private long createTime;
    private String description;
    private long modifyTime;
    private String nickname;
    private int onlinestate;
    private int sex;
    private String telephone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getOnlinestate() {
        return onlinestate;
    }

    public void setOnlinestate(int onlinestate) {
        this.onlinestate = onlinestate;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", userType=" + userType +
                ", birthday=" + birthday +
                ", createTime=" + createTime +
                ", description='" + description + '\'' +
                ", modifyTime=" + modifyTime +
                ", nickname='" + nickname + '\'' +
                ", onlinestate=" + onlinestate +
                ", sex=" + sex +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
