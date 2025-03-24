package com.sprint.mission.discodeit.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class CreatePrivateChannelDto {
    private Set<UUID> users;
}
