package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 100L;
    // 메시지 내용
    private String content;
    // 메시지 작성자 id
    private UUID userid;
    // 메시지 작성 채널 id
    private UUID channelid;

    // id를 받는 간접 참조
    public Message(String content, UUID userid, UUID channelid){
        super();
        this.content = content;
        this.userid = userid;
        this.channelid = channelid;
    }

    public String getContent() { return content; }
    public UUID getUser() { return userid; }
    public UUID getChannel() { return channelid; }
    public void updateMessage(String content){
        this.content = content;
        updateTimestamp();
    }
}
