package com.sprint.mission.discodeit.entity;

import java.util.*;
import java.util.UUID;

public class Message {
    private UUID id;
    private String Sender;
    private String Receiver;
    private String Message;
    private Long cratedAt;
    private Long updateAt;//메세지 저장

    public Message(String Sender, String Receiver, String Message) {
        this.id = UUID.randomUUID();
        this.cratedAt = new Date().getTime();
        this.Sender = Sender;//보내는 사람
        this.Receiver = Receiver;//받는사람
        this.Message = Message; //메세지 내용
    }

    public String getMessage() {

        return Message;
    }
    public String getSender() {
        return Sender;
    }
    public String getReceiver() {
        return Receiver;
    }

    public UUID getId() {
        return id;
    }

    public Long getCratedAt() {
        return cratedAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }
    public void UpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }
}


