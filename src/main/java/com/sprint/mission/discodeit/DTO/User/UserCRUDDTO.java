package com.sprint.mission.discodeit.DTO.User;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserCRUDDTO(
        UUID userId,
        UUID profileId,
        String userName,
        String email,
        String password,
        BinaryContent binaryContent
) {
    public static UserCRUDDTO checkDuplicate(String userName,
                                             String email) {
        return UserCRUDDTO.builder().userName(userName).email(email).build();
    }

    public static UserCRUDDTO login(String userName,
                                    String password) {
        return UserCRUDDTO.builder()
                .userName(userName)
                .password(password).build();
    }

    public static UserCRUDDTO create(String userName,
                                     String email,
                                     String password) {
        return UserCRUDDTO.builder()
                .userName(userName)
                .email(email)
                .password(password).build();
    }

    public static UserCRUDDTO delete(UUID userId) {
        return UserCRUDDTO.builder()
                .userId(userId).build();
    }

    public static UserCRUDDTO update(UUID replaceId,
                                     UUID replaceProfileId,
                                     String replaceName,
                                     String replaceEmail) {
        return UserCRUDDTO.builder()
                .userId(replaceId)
                .profileId(replaceProfileId)
                .userName(replaceName)
                .email(replaceEmail).build();
    }
}
