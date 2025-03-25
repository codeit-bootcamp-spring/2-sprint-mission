package com.sprint.discodeit.service.basic.util;

import com.sprint.discodeit.domain.dto.userDto.UserLoginRequestDto;
import com.sprint.discodeit.repository.file.FileUserRepository;
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
