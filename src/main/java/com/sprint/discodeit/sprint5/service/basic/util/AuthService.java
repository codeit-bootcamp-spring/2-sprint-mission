package com.sprint.discodeit.sprint5.service.basic.util;

import com.sprint.discodeit.sprint5.domain.dto.userDto.UserLoginRequestDto;
import com.sprint.discodeit.sprint5.repository.file.FileUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final FileUserRepository fileUserRepository;

    public void authenticateUser(UserLoginRequestDto loginRequestDto) {
        if(fileUserRepository.findByUsername(loginRequestDto.username()).isPresent()){
            throw new IllegalArgumentException("이미 있는 닉네임 입니다.");
        }
    }


}
