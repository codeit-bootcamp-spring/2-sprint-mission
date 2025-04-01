package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class CreatePrivateChannelRequest {
    private Set<UUID> users;
}
