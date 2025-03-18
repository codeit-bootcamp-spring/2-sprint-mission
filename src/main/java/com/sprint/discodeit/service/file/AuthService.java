package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.dto.UserLoginRequestDto;
import com.sprint.discodeit.repository.UserRepository;
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
