package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.User.UserLoginDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
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
                .findFirst().orElseThrow(() -> CommonExceptions.USER_NOT_FOUND);
        boolean login = findUser.getPassword().equals(userLoginDTO.password());
        if (login == false) {
            throw CommonExceptions.INVALID_PASSWORD;
        }
        return findUser;
    }
}
