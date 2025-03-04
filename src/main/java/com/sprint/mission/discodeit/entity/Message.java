package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message extends Common implements Serializable {

    private static final long serialVersionUID = 1L;

    // 메시지 내용
    private String content;
    // 메시지를 보낸 유저
    // 채널에 보내는 메시지 이므로 보낸 유저만 있으면 될 것 같다
    private final User sender;
    // 메시지가 보내지는 채널
    private final Channel channel;

    public Message(String content, User sender, Channel channel) {
        this.content = content;
        this.sender = sender;
        this.channel = channel;
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

    // channel과 sender는 객체 생성할 때 정해지고 바뀔 일이 없다.

    // 메시지 수정
    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", sender=" + sender +
                ", channel=" + channel +
                '}';
    }
}
