package com.sprint.discodeit.sprint.service.basic.users;


import com.sprint.discodeit.sprint.domain.dto.UsersIdAllResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersLoginRequestDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersLoginResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersNameStatusResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersProfileImgResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersRequestDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersUpdateRequestDto;

public interface UsersService {

    UsersNameStatusResponseDto create(UsersRequestDto usersRequestDto, UsersProfileImgResponseDto usersProfileImgResponseDto);

    UsersResponseDto find(Long usersId);

    UsersIdAllResponseDto findAll();

    UsersResponseDto update(UsersUpdateRequestDto usersUpdateRequestDto, Long usersId);

    void delete(Long usersId);

    UsersLoginResponseDto login(UsersLoginRequestDto usersLoginRequestDto);
}
