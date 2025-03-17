package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.auth.LoginParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.AuthService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserDTO login(LoginParam loginParam) {
        User user = userRepository.findByUsernameAndPassword(loginParam.username(), loginParam.password())
                .orElseThrow(() -> new NoSuchElementException("해당 username과 password와 일치하는 유저가 없습니다."));
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
        return UserMapper.userEntityToDTO(user, userStatus);
    }
}
