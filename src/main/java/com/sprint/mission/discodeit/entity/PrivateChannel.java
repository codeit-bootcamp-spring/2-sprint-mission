package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;

public class PrivateChannel extends Channel {
    private List<UUID> participants; // 참여하는 유저들의 ID 목록

    public PrivateChannel(List<UUID> participants) {
        super(ChannelType.PRIVATE, null, null); // 이름과 설명은 생략
        this.participants = participants;
    }

    public List<UUID> getParticipants() {
        return participants;
    }
}