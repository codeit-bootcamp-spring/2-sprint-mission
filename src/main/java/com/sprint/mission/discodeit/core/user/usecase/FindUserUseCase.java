package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import java.util.List;
import java.util.UUID;

public interface FindUserUseCase {


  List<UserDto> findAll();

}
