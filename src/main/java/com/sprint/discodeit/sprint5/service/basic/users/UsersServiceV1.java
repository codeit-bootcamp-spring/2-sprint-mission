package com.sprint.discodeit.sprint5.service.basic.users;

import com.sprint.discodeit.sprint5.domain.dto.usersDto.usersLoginRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.usersDto.usersLoginResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.usersDto.usersNameStatusResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.usersDto.usersProfileImgResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.usersDto.usersRequestDto;
import com.sprint.discodeit.sprint5.domain.dto.usersDto.usersResponseDto;
import com.sprint.discodeit.sprint5.domain.dto.usersDto.usersUpdateRequestDto;
import com.sprint.discodeit.sprint5.domain.entity.users;
import java.util.List;
import java.util.UUID;

public interface UsersServiceV1 {

    usersNameStatusResponseDto create(usersRequestDto usersRequestDto, usersProfileImgResponseDto usersProfileImgResponseDto);
    usersResponseDto find(UUID usersId);
    List<users> findAll();
    usersResponseDto update(usersUpdateRequestDto usersUpdateRequestDto, String usersId);
    void delete(UUID usersId);
    usersLoginResponseDto login(usersLoginRequestDto usersLoginRequestDto);
}
