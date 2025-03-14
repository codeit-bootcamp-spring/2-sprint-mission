package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User login(String username, String password) {
        User userInfo = userRepository.findUserByUsername(username)
                .orElseGet(() -> {
                    System.out.println("사용자를 찾을 수 없습니다.");
                    return null;
                });

        if (userInfo == null) {
            return null;
        }

        if (!userInfo.getPassword().equals(password)) {
            System.out.println("[실패]비밀번호가 일치하지 않습니다.");
            return null;
        }

        userStatusRepository.update(userInfo.getId());

        System.out.println("[성공] 로그인 완료");
        return userInfo;
    }
}
