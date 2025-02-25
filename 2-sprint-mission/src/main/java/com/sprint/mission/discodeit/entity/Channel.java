package com.sprint.mission.discodeit.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private ChannelType type;
    private String category;
    private String name;
    private final UUID userId;
    private final Set<UUID> userMembers;
    private UserRole writePermission;

    public Channel(ChannelType type, String category, String name, UUID userId, UserRole writePermission) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.type = type;
        this.category = category;
        this.name = name;
        this.userId = userId;
        this.userMembers = new HashSet<>();
        this.writePermission = writePermission;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public ChannelType getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public UUID getUserId() {
        return userId;
    }

    public Set<UUID> getUserMembers() {
        //return members;
        return Collections.unmodifiableSet(userMembers);
    }

    public UserRole getWritePermission() {
        return writePermission;
    }

    public void updateType(ChannelType type) {
        this.type = type;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateCategory(String category) {
        this.category = category;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateWritePermission(UserRole writePermission) {
        this.writePermission = writePermission;
    }

    public void addMember(UUID memberId){
        this.userMembers.add(memberId);
    }

    public void removeMember(UUID memberId){
        this.userMembers.remove(memberId);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", userMembers=" + userMembers + '\'' +
                ", writePermission=" + writePermission +
                '}';
    }
}
