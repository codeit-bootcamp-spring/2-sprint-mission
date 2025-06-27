package com.sprint.mission.discodeit.core.user.service.usecase;

import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.user.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.core.user.dto.response.UserDto;
import java.util.Optional;
import java.util.UUID;

public interface UpdateUserUseCase {

  UserDto update(UUID id, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> binaryContentDTO);

}
