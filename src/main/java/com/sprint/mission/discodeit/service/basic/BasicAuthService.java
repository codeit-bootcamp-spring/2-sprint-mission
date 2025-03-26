package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.AuthLoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public AuthLoginResponseDto login(AuthLoginRequestDto requestLoginDto) {
        User matchedUser = userRepository.findAll().stream()
                .filter(user -> user.getName().equals(requestLoginDto.userName()))
                .filter((user -> user.getPwd().equals(requestLoginDto.userPwd())))
                .findFirst()
                .orElse(null);

        if (matchedUser == null) {
            throw new IllegalArgumentException("[Error] matchedUser is null");
        }

        return new AuthLoginResponseDto(matchedUser.getUuid(), matchedUser.getName(), matchedUser.getEmail());
    }


}
