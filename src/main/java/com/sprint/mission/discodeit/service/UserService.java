package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto save(UserCreateRequest userCreateRequest, MultipartFile profile)
      throws IOException;

  List<UserDto> findAllUser();

  UserDto update(UUID userId, UserUpdateRequest updateUserDto, MultipartFile profile)
      throws IOException;

  void delete(UUID userId);
}
