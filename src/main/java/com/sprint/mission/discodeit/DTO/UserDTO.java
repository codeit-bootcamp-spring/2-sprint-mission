package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.User;

public record UserDTO(
        String username,
        String email,
        byte[] profileImage
) {
    public static UserDTO fromName(String username) {
        return new UserDTO(username, username + "@example.com", null);
    }

    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getUserName(),
                user.getEmail(),
                user.getProfileImage() != null ? user.getProfileImage().getData() : null
        );
    }
}
