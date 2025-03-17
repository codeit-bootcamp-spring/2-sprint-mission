package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    public User loginUser(UserCRUDDTO userLoginDTO) {
        List<User> list = userRepository.findUserList();
        User user = CommonUtils.findByName(list, userLoginDTO.userName(), User::getName)
                .orElseThrow(() -> CommonExceptions.USER_NOT_FOUND);
        boolean login = user.getPassword().equals(userLoginDTO.password());
        if (login == false) {
            throw CommonExceptions.INVALID_PASSWORD;
        }
        return user;
    }
}
