package com.sprint.discodeit.sprint.service.basic.users;


import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersLoginRequestDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersLoginResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersNameStatusResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersProfileImgResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersRequestDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.Users;
import java.util.List;

public interface UsersService {

    UsersNameStatusResponseDto create(UsersRequestDto usersRequestDto, UsersProfileImgResponseDto usersProfileImgResponseDto);

    UsersResponseDto find(Long usersId);

    List<Users> findAll();

    UsersResponseDto update(UsersUpdateRequestDto usersUpdateRequestDto, String usersId);

    void delete(Long usersId);

    UsersLoginResponseDto login(UsersLoginRequestDto usersLoginRequestDto);
}
