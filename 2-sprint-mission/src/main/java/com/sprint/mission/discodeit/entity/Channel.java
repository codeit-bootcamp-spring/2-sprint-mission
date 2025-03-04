package com.sprint.mission.discodeit.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Channel extends BaseEntity {
    private ChannelType type;
    private String category;
    private String name;
    private final UUID userId;
    private final Set<UUID> userMembers;
    private UserRole writePermission;

    public Channel(ChannelType type, String category, String name, UUID userId, UserRole writePermission) {
        super();
        this.type = type;
        this.category = category;
        this.name = name;
        this.userId = userId;
        this.userMembers = new HashSet<>();
        this.writePermission = writePermission;
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
        updateTimestamp();
    }

    public void updateCategory(String category) {
        this.category = category;
        updateTimestamp();
    }

    public void updateName(String name) {
        this.name = name;
        updateTimestamp();
    }

    public void updateWritePermission(UserRole writePermission) {
        this.writePermission = writePermission;
        updateTimestamp();
    }

    public void addMember(UUID memberId){
        this.userMembers.add(memberId);
        updateTimestamp();
    }

    public void removeMember(UUID memberId){
        this.userMembers.remove(memberId);
        updateTimestamp();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + getId() +
                ", type=" + type +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", userMembers=" + userMembers +
                ", writePermission=" + writePermission +
                '}';
    }
}
