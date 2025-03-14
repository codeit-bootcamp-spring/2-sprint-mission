package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.User.UserLoginDTO;
import com.sprint.mission.discodeit.Exception.InvalidPasswordException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User loginUser(UserLoginDTO userLoginDTO) {
        List<User> list = userRepository.findUserList();
        User findUser = list.stream().filter(u -> u.getName().equals(userLoginDTO.userName()))
                .findFirst().orElseThrow(() -> new UserNotFoundException("로그인할 유저를 찾을 수 없습니다."));
        boolean login = findUser.getPassword().equals(userLoginDTO.password());
        if (login == false) {
            throw new InvalidPasswordException("패스워드가 일치하지 않습니다.");
        }
        return findUser;
    }
}
