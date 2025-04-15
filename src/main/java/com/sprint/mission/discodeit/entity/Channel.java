package com.sprint.mission.discodeit.entity;

// 필요한 import 추가

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Collection; // findPrivateChannels 타입 수정

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Channel extends BaseUpdatableEntity {

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @Column(unique = true)
    private String name;
    private String description;

    @Column(nullable = false)
    private UUID ownerId; // 우선 ID 유지

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "channel_users", joinColumns = @JoinColumn(name = "channel_id"))
//    @Column(name = "user_id")
//    private List<UUID> userList = new ArrayList<>(); // Null 방지


    public Channel(String name, UUID ownerId, String description) {
        super();
        this.type = ChannelType.PUBLIC;
        this.name = name;
        this.ownerId = ownerId;
        this.description = description;
    }

    public Channel(String name, UUID ownerId) {
        super();
        this.type = ChannelType.PUBLIC;
        this.name = name;
        this.ownerId = ownerId;
    }

    public Channel(UUID ownerId, List<UUID> participantIds) {
        super();
        this.type = ChannelType.PRIVATE;
        this.ownerId = ownerId;
        if (participantIds != null) { // null 방지
            this.userList = new ArrayList<>(participantIds); // 복사
        }
        // 참여자 목록에 owner도 포함 (일반적으로 DM방은 생성자도 포함)
        if (!this.userList.contains(ownerId)) {
            this.userList.add(ownerId);
        }
        this.name = "Private_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public Channel(String channelName, UUID ownerId, List<UUID> participantIds) {
        super();
        this.type = ChannelType.PRIVATE;
        this.name = channelName;
        this.ownerId = ownerId;
        if (participantIds != null) { // null 방지
            this.userList = new ArrayList<>(participantIds); // 복사
        }
        // 참여자 목록에 owner도 포함
        if (!this.userList.contains(ownerId)) {
            this.userList.add(ownerId);
        }
    }

    // update 메서드 유지
    public boolean update(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.isEmpty() && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
        // description은 null로 업데이트 될 수도 있음
        if (newDescription == null && this.description != null) {
            this.description = null;
            anyValueUpdated = true;
        } else if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }
        return anyValueUpdated;
    }


    // isPublic, isPrivate 메서드 유지
    public boolean isPublic() {
        return this.type == ChannelType.PUBLIC;
    }

    public boolean isPrivate() {
        return this.type == ChannelType.PRIVATE;
    }


    // leaveChannel, addParticipant 메서드 유지
    public void leaveChannel(UUID userId) {
        if (this.userList != null) {
            this.userList.remove(userId);
        }
    }

    public void addParticipant(UUID userId) {
        if (this.userList == null) {
            this.userList = new ArrayList<>();
        }
        if (!this.userList.contains(userId)) {
            this.userList.add(userId);
        }
    }
}