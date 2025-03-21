package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserLoginRequestDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFound.UserNotFoundException;
import com.sprint.mission.discodeit.exception.Valid.InvalidPasswordException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @CustomLogging
    @Override
    public boolean loginUser(UserLoginRequestDTO userLoginDTO) {
        List<User> list = userRepository.findAll();
        User user = CommonUtils.findByName(list, userLoginDTO.userName(), User::getName)
                .orElseThrow(() -> new UserNotFoundException("로그인 실패: 해당 유저를 찾지 못했습니다."));
        boolean login = user.getPassword().equals(userLoginDTO.password());
        if (login == false) {
            throw new InvalidPasswordException("로그인 실패: 비밀번호가 틀립니다.");
        }
        return login;
    }
}
