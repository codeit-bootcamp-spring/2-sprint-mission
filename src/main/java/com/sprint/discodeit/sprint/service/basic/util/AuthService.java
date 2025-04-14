package com.sprint.discodeit.sprint.service.basic.util;

import com.sprint.discodeit.sprint.domain.dto.usersDto.usersLoginRequestDto;
import com.sprint.discodeit.sprint.repository.file.FileUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final FileUsersRepository fileusersRepository;

    public void authenticateusers(usersLoginRequestDto loginRequestDto) {
        if(fileusersRepository.findByusersname(loginRequestDto.usersname()).isPresent()){
            throw new IllegalArgumentException("이미 있는 닉네임 입니다.");
        }
    }


}
