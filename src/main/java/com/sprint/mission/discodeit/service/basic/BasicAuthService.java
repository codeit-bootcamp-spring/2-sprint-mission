package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(AuthServiceLoginDto authServiceLoginDto) {
        List<User> UserList = userRepository.load();
        User matchingUserUser = UserList.stream().filter(m -> m.getName().equals(authServiceLoginDto.name())).findAny()
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (!matchingUserUser.getPassword().equals(authServiceLoginDto.password())) {
            throw new NoSuchElementException("Password does not match");
        }
        System.out.println("Login successful");
        return matchingUserUser;
    }
}
