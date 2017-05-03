package com.seatel.im.service;

import lombok.Data;

/**
 * Created by eldorado on 17-5-3.
 *
 * 群聊房间的配置信息
 */
@Data
public class MucInfo {
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
}
