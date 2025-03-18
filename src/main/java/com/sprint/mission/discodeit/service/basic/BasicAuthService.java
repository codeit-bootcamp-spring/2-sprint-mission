package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Request.UserLoginDTO;
import com.sprint.mission.discodeit.Exception.NotFound.UserNotFoundException;
import com.sprint.mission.discodeit.Exception.Valid.InvalidPasswordException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @CustomLogging
    @Override
    public boolean loginUser(UserLoginDTO userLoginDTO) {
        List<User> list = userRepository.findUserList();
        User user = CommonUtils.findByName(list, userLoginDTO.userName(), User::getName)
                .orElseThrow(() -> new UserNotFoundException("로그인 실패: 해당 유저를 찾지 못했습니다."));
        boolean login = user.getPassword().equals(userLoginDTO.password());
        if (login == false) {
            throw new InvalidPasswordException("로그인 실패: 비밀번호가 틀립니다.");
        }
        return login;
    }
}
