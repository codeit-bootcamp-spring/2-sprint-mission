package com.sprint.mission.discodeit.dto.UserService;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.NoSuchElementException;

public record UserFindDto(
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
