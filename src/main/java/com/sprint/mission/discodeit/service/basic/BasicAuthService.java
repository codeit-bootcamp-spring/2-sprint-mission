package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginRep;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public AuthServiceLoginRep login(AuthServiceLoginReq authServiceLoginReq) {
        List<User> UserList = userRepository.load();
        User matchingUserUser = UserList.stream().filter(m -> m.getName().equals(authServiceLoginReq.name())).findAny()
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!matchingUserUser.getPassword().equals(authServiceLoginReq.password())) {
            throw new InvalidInputException("Password does not match");
        }

        return AuthServiceLoginRep.authLogin(true, "login success");
    }
}
