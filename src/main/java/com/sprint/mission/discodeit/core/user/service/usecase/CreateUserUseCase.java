package com.sprint.mission.discodeit.core.user.service.usecase;

import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.response.UserDto;
import java.util.Optional;

public interface CreateUserUseCase {

  UserDto create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentDTO);

}
