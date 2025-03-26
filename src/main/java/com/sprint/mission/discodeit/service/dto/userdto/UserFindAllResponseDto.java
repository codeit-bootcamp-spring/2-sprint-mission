package com.sprint.mission.discodeit.service.dto.userdto;

import com.sprint.mission.discodeit.entity.CurrentStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UserFindAllResponseDto(
        UUID userId,
        String name,
        String email,
        UUID profileId,
        Instant createdAt,
        CurrentStatus userStatus
) {
    public static List<UserFindAllResponseDto> UserFindAllResponse(List<User> users, List<UserStatus> userStatuses) {
        List<UserFindAllResponseDto> FindAllResponse = new ArrayList<>();

        for (User user : users) {
            UserStatus matchingUserStatus = userStatuses.stream()
                    .filter(m ->m.getUserId().equals(user.getId()))
                    .findAny()
                    .orElse(null);
            if (matchingUserStatus != null) {
                UserFindAllResponseDto responseDto = new UserFindAllResponseDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getProfileId(),
                        user.getCreatedAt(),
                        matchingUserStatus.currentUserStatus()
                );
                FindAllResponse.add(responseDto);
            }
        }
    return FindAllResponse;
    }
}
