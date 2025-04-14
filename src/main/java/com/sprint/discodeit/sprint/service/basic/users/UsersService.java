package com.sprint.discodeit.sprint.service.basic.users;

import com.sprint.discodeit.sprint.domain.dto.usersDto.usersLoginRequestDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.usersLoginResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.usersNameStatusResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.usersProfileImgResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.usersRequestDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.usersResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.usersUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Users;
import java.util.List;

public interface UsersService {

    usersNameStatusResponseDto create(usersRequestDto usersRequestDto,usersProfileImgResponseDto usersProfileImgResponseDto);

    usersResponseDto find(Long usersId);

    List<Users> findAll();

    usersResponseDto update(usersUpdateRequestDto usersUpdateRequestDto, String usersId);

    void delete(Long usersId);

    usersLoginResponseDto login(usersLoginRequestDto usersLoginRequestDto);
}
