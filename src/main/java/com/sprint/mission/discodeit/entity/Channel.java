package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    // 채널명
    private String channelName;
    // 해당 채널의 유저리스트
    private List<User> users = new ArrayList<>();

    public Channel(String channelName, List<User> users) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.channelName = channelName;
        this.users = users;
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

    public String getChannelName() {
        return channelName;
    }

    public List<User> getUsers() {
        return users;
    }


    // id, createdAt은 생성하고 바뀔 일이 없다.

    // 업데이트시간 수정
    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    // 채널명 수정
    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    // 유저 수정 - 채널에 유저가 들어가고 나갈 수 있음
    public void updateUsers(List<User> users) {
        this.users = users;
    }
}
