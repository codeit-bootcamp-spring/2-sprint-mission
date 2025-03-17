package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.model.ChannelType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2L;
    private ChannelType channelType;
    private String channelName;
    private Set<UUID> participantIds;     //중복X
    @Setter
    private Instant lastMessageTime;

    // private channel 생성자
    public Channel(ChannelType channelType) {
        super();
        this.channelType = channelType;
        this.channelName = null;        // channel name이 null 일 수 있으니 getter 메서드를 호출할 때 주의!
        this.participantIds = new HashSet<>();
        this.lastMessageTime = null;
    }

    // public channel 생성자
    public Channel(ChannelType channelType, String channelName) {
        super();
        this.channelType = channelType;
        validateChannelName(channelName);
        this.channelName = channelName;
        this.participantIds = new HashSet<>();
        this.lastMessageTime = null;        // 채널이 생성된 직후 lastMessageTime 값을 null로 초기화
    }

    public void updateChannelName(String newChannelName) {
        validateChannelName(newChannelName);
        this.channelName = newChannelName;
        super.updateUpdatedAt();
    }

    public List<UUID> getParticipantIds() {
        return new ArrayList<>(this.participantIds);
    }

    public void addParticipant(UUID newParticipantId) {
        if (this.participantIds.contains(newParticipantId)) {
            throw new IllegalArgumentException("newParticipant 는 이미 채널에 참여 중 입니다!!! ");
        }
        this.participantIds.add(newParticipantId);
        super.updateUpdatedAt();
    }

    public static void validateChannelName(String channelName) {
        if (channelName == null || channelName.trim().isEmpty()) {
            throw new IllegalArgumentException("channelName 은 null 이거나 공백일 수 없다!!!");
        }
    }

    @Override
    public String toString() {
        return "\nChannel\n"
                + "channelType: " + channelType + "\n"
                + "channelName: " + channelName + '\n'
                + "participants:\n" + participantIds + '\n'
                + "id: " + super.getId() + '\n'
                + "createdAt: " + super.getCreatedAt() + '\n'
                + "updatedAt: " + super.getUpdatedAt() + '\n';
    }
}
