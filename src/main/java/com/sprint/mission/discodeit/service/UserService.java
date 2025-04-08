package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserCreateResponse save(UserCreateRequest userCreateRequest, MultipartFile profile)
      throws IOException;

  FindUserDto findByUser(UUID userId);

  List<FindUserDto> findAllUser();

  UserUpdateResponse update(UUID userId, UserUpdateRequest updateUserDto, MultipartFile profile)
      throws IOException;

  void delete(UUID userId);
}
