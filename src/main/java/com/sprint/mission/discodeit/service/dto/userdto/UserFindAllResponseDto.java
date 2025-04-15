package com.sprint.mission.discodeit.service.dto.userdto;

import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UserFindAllResponseDto(
        UUID id,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
    public static List<UserFindAllResponseDto> UserFindAllResponse(List<User> users) {
        List<UserFindAllResponseDto> FindAllResponse = new ArrayList<>();
        users.stream().map(user-> new UserFindAllResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile().getId() == null ? null : user.getProfile().getId(),
                user.getStatus().currentUserStatus()))
                .forEach(FindAllResponse::add);
        return FindAllResponse;
    }
}
