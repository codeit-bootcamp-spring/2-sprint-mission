package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    // 메시지 내용
    private String content;
    // 메시지를 보낸 유저
    // 채널에 보내는 메시지 이므로 보낸 유저만 있으면 될 것 같다
    private User sender;
    // 메시지가 보내지는 채널
    private Channel channel;

    public Message(String content, User sender, Channel channel) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.content = content;
        this.sender = sender;
        this.channel = channel;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public Channel getChannel() {
        return channel;
    }

    // id, 그리고 Channel과 Sender는 객체 생성할 때 정해지고 바뀔 일이 없다.

    // 수정 날짜 수정
    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    // 메시지 수정
    public void updateContent(String content) {
        this.content = content;
    }




}
