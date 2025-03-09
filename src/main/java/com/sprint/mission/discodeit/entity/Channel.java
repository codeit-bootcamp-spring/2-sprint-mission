package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 102L;

    private final UUID channelId; // 채널 ID
    private final Long createdAt; // 생성시간
    private Long updateAt; // 업데이트 된 시간
    private final UUID ownerID; // 채널 생성자의 아이디
    private final Set<UUID> userList = new HashSet<>(); // 가입한 유저 ID 리스트
    private String channelName;
    
    public Channel(String channelName, UUID ownerID) {
        this.createdAt = new Date().getTime(); // 객체 생성 시간
        this.channelId = UUID.randomUUID();
        this.channelName = channelName;
        this.ownerID = ownerID;
    }

    public UUID getChannelId() {
        return channelId;
    }
    
    public String getChannelName() {
        return channelName;
    }
    
    public Long getCreatedAt() {
        return createdAt;
    }
    
    public Set<UUID> getUserList() {
        return userList;
    }
    
    public void joinChannel(UUID userId) {
        this.getUserList().add(userId);
    }
    
    public void leaveChannel(UUID userId) {
        this.getUserList().remove(userId);
    }

    public void setChannelName(String newChannelName) {
        this.channelName = newChannelName;
        setUpdateAt();
    }
    
    public Long getUpdateAt() {
        return updateAt;
    }
    
    public void setUpdateAt() {
        this.updateAt = new Date().getTime();
    }
    
    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public UUID getOwnerID() {
        return ownerID;
    }
}
