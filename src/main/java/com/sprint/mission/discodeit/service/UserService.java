package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.service.user.CreateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.CreateUserResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserCommand;
import com.sprint.mission.discodeit.dto.service.user.UpdateUserResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

  CreateUserResult create(CreateUserCommand createUserCommand, MultipartFile multipartFile);

  FindUserResult find(UUID id);

  List<FindUserResult> findAll();

  UpdateUserResult update(UUID id, UpdateUserCommand updateUserCommand,
      MultipartFile multipartFile);

  void delete(UUID id);

  ;
}
