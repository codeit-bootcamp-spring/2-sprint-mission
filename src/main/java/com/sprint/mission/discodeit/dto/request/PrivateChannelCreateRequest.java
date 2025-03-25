package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class PrivateChannelCreateRequest {
    private List<UUID> participantIds;

    @Builder
    public PrivateChannelCreateRequest(List<UUID> participantIds) {
        this.participantIds = participantIds;
    }
}
