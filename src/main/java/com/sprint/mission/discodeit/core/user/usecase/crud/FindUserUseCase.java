package com.sprint.mission.discodeit.core.user.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.user.dto.UserFindDTO;
import java.util.List;
import java.util.UUID;

public interface FindUserUseCase {

  UserFindDTO findById(UUID userId);

  boolean existsById(UUID userId);

  List<UserFindDTO> listAllUsers();

}
