package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentUpdateDto;
import com.sprint.mission.discodeit.service.dto.userdto.*;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto);
    UserFindResponseDto find(UserFindRequestDto userFindRequestDto);
    List<UserFindAllResponseDto> findAllUser();
    User update(UserUpdateDto userUpdateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto);
    void delete(UserDeleteDto userDeleteDto);
}
