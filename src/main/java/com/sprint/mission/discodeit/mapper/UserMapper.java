package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toResponse(User user) {
    BinaryContent profile = user.getProfile();
    BinaryContentDto profileDto = (profile == null) ? null : binaryContentMapper.toDto(profile);
    
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        profileDto,
        user.getStatus().isOnline()
    );
  }
}

