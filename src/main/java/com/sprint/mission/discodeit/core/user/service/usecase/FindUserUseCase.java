package com.sprint.mission.discodeit.core.user.service.usecase;

import com.sprint.mission.discodeit.core.user.dto.UserDto;
import java.util.List;

public interface FindUserUseCase {


  List<UserDto> findAll();

}
