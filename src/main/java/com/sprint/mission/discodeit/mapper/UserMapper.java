package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user) {
    if (user == null) {
      return null;
    }
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(binaryContentMapper.toDto(user.getProfile()))
        .online(user.getUserStatus() != null ? user.getUserStatus().isOnline() : null)
        .build();
  }

  public User toEntity(UserCreateRequest userCreateRequest) {
    if (userCreateRequest == null) {
      return null;
    }
    return new User(userCreateRequest.username(), userCreateRequest.email(),
        userCreateRequest.password());
  }
}
