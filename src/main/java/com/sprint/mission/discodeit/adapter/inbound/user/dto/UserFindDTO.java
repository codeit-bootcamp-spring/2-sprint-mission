package com.sprint.mission.discodeit.adapter.inbound.user.dto;

import com.sprint.mission.discodeit.core.user.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserFindDTO(
        UUID id,
        UUID profileId,
        String name,
        String email,
        Instant createdAt,
        Instant updatedAt,
        boolean online
) {
    public static UserFindDTO create(User user, boolean online) {
        return UserFindDTO.builder()
                .id(user.getId())
                .profileId(user.getProfileId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .online(online).build();
    }
}
