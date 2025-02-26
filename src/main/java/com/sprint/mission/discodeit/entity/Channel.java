package com.sprint.mission.discodeit.entity;

// 채팅방을 관리하는 클래스
public class Channel extends BaseEntity {
    // 채널 이름
    private String name;
    // 채널 주제
    private String topic;

    public Channel(String name, String topic){
        super();
        this.name = name;
        this.topic = topic;
    }

    public String getName() { return name; }
    public String getTopic() { return topic; }
    public void updateChannel(String name, String topic){
        this.name = name;
        this.topic = topic;
        updateTimestamp();
    }
}
