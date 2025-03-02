package com.sprint.mission.discodeit.entity;

import java.awt.*;
import java.util.Date;
import java.util.UUID;

public class Channel {
    private User id;//유저 아이디를 가져야함
    private UUID channelId; //채널 이름
    private Long cratedAt; //생성시간
    private Long updateAt; //업데이트 된 시간
    private UUID ornerID; //채널생성자
    private  String channelname;
    public Channel(String channelname) {
       this.cratedAt=new Date().getTime(); //객체 생성 시간
       this.channelId = UUID.randomUUID();
       this.channelname =channelname;

    }
    public UUID getId() {
        return channelId;
    } //
    public String getChannelName() {return channelname;}
    public Long getCratedAt() {
        return cratedAt;
    }
    public Long getUpdateAt() {
        return updateAt;
    }
//    public void UpdateAt(Long updateAt) {
//        this.updateAt = updateAt;
//    }
}
