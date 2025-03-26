package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.service.user.CreateUserParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

// User <-> DTO 변환 로직이 공통으로 쓰이는 곳이 있어 따로 클래스로 분리
public class UserMapper {
    public static UserDTO userEntityToDTO(User user, UserStatus userStatus) {
        return UserDTO.builder()
                .isLogin(userStatus.isLoginUser())
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .profileId(user.getProfileId())
                .build();
    }

}
