package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Channel implements Serializable {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private ChannelType type;
    private String category;
    private String name;
    private final UUID userId;
    private final Set<UUID> userMembers;
    private UserRole writePermission;
    private static final long serialVersionUID = 1L;

    public Channel(ChannelType type, String category, String name, UUID userId, UserRole writePermission) {
        validateChannel(type, category, name, userId, writePermission);
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
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

    public void update(String name, String category, ChannelType type) {
        boolean isUpdated = false;

        if (name != null && !name.equals(this.name)) {
            validateName(name);
            this.name = name;
            isUpdated = true;
        }
        if (category != null && !category.equals(this.category)) {
            validateCategory(category);
            this.category = category;
            isUpdated = true;
        }
        if (type != null && !type.equals(this.type)) {
            this.type = type;
            isUpdated = true;
        }

        if (isUpdated) {
            updateLastModifiedAt();
        }
    }

    public void addMember(UUID memberId){
        if (userMembers.contains(memberId)) {
            throw new RuntimeException("이미 채널에 포함된 사용자입니다: " + memberId);
        }
        this.userMembers.add(memberId);
        updateLastModifiedAt();
    }

    public void removeMember(UUID memberId){
        if (!userMembers.contains(memberId)) {
            throw new RuntimeException("채널에 존재하지 않는 사용자입니다: " + memberId);
        }
        this.userMembers.remove(memberId);
        updateLastModifiedAt();
    }

    private void updateLastModifiedAt() {
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", type=" + type +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", userMembers=" + userMembers +
                ", writePermission=" + writePermission +
                '}';
    }

    /*******************************
     * Validation check
     *******************************/
    private void validateChannel(ChannelType type, String category, String name, UUID userId, UserRole writePermission){
        // 1. null check
        if (type == null) {
            throw new IllegalArgumentException("Type 값이 없습니다.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("채널명이 없습니다.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("채널 개설자의 ID가 없습니다.");
        }
        if (writePermission == null) {
            throw new IllegalArgumentException("채널 권한 값이 없습니다.");
        }
        // 2. 카테고리명 길이 check
        validateCategory(category);
        // 3. 채널명 길이 check
        validateName(name);
    }

    private void validateCategory(String category){
        if (category != null && category.length() > 20) {
            throw new IllegalArgumentException("카테고리명은 20자 미만이어야 합니다.");
        }
    }

    private void validateName(String name){
        if (name != null && name.length() > 20) {
            throw new IllegalArgumentException("채널명은 20자 미만이어야 합니다.");
        }
    }

}
