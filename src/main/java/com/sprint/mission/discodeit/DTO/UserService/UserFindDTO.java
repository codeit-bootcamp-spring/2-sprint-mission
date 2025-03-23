package com.sprint.mission.discodeit.DTO.UserService;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.NoSuchElementException;
import java.util.UUID;

public record UserFindDTO(
        User user,
        UserStatus userStatus
) {
    @Override
    public String toString() {
        try {
            return user.toString() + userStatus.toString();
        }catch(Exception e) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }
    }
}
