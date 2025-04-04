package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.userdto.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {
    User create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto);
    UserFindResponseDto find(UserFindRequestDto userFindRequestDto);
    List<UserFindAllResponseDto> findAllUser();
    User update(UUID userId, UserUpdateDto userUpdateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto);
    void delete(UUID userId);
}
