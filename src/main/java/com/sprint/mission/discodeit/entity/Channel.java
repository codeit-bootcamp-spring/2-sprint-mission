package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;

@Getter
public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2L;
    private String channelName;
    private Set<UUID> participantIds;     //중복X && 이름순으로 정렬

    public Channel(String channelName) {
        super();
        validateChannelName(channelName);
        this.channelName = channelName;
        this.participantIds = new HashSet<>();
    }

    public void updateChannelName(String newChannelName) {
        validateChannelName(newChannelName);
        this.channelName = newChannelName;
        super.updateUpdatedAt();
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
                + "channelName: " + channelName + '\n'
                + "participants:\n" + participantIds + '\n'
                + "id: " + super.getId() + '\n'
                + "createdAt: " + super.getCreatedAt() + '\n'
                + "updatedAt: " + super.getUpdatedAt() + '\n';
    }
}
