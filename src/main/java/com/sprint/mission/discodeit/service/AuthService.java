package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

  UserDto register(CreateUserRequest request, MultipartFile profile);

  UserDto login(LoginRequest request);
}
