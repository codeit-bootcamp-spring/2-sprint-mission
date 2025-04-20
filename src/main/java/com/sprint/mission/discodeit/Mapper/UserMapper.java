package com.sprint.mission.discodeit.Mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final UserStatusRepository userStatusRepository;

    public UserDto toDto(User user) {
        BinaryContentDto binaryContent = user.getProfile() == null ? null : binaryContentMapper.toDto(user.getProfile());

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profile(binaryContent)
                .online(userStatusRepository.findByUserId(user.getId())
                        .map(UserStatus::isOnline)
                        .orElse(false))
                .build();
    }
}
