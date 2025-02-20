package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Channel {
    private static int count;
    private final SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public final Long createdAt;
    public Long updatedAt;

    protected final String id;
    private String name;

    private Message defaultMessage;
    private final List<Message> messagesList;


//    private Map<String, Message> subChanel = new HashMap<>();

    public Channel(String name, Message message) {
        this.id = "C" + count++;
        this.name = name;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.messagesList = new ArrayList<>();
        this.defaultMessage = message;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Message getDefaultMessage() {
        return defaultMessage;
    }

    public Long getCreatedAt() {
        System.out.println("생성 시각: " + dayTime.format(new Date(createdAt)));
        return createdAt;
    }

    public Long getUpdatedAt() {
        System.out.println("수정 시각: " + dayTime.format(new Date(updatedAt)));
        return updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultMessage(Message defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void update() {
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "\ncreatedAt=" + dayTime.format(new Date(createdAt)) +
                ",\nupdatedAt=" + dayTime.format(new Date(updatedAt)) +
                ",\nid='" + id + '\'' +
                ",\nname='" + name + '\'' +
                ",\ndefaultMessage=" + defaultMessage +
                '}';
    }
}
