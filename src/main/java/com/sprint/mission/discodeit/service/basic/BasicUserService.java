package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public void save(String username, String password, String nickname, String profile) {
        User user = userRepository.save(username, password, nickname, profile);

        if (user == null) {
            System.out.println("[실패] 저장 실패.");
            return;
        }

        System.out.println("[성공]" + user);
    }

    @Override
    public User findByUser(UUID userUUID) {
        return userRepository.findUserById(userUUID)
                .orElseGet(() -> {
                    System.out.println("[실패] 사용자가 존재하지 않습니다.");
                    return null;
                });
    }

    @Override
    public List<User> findAllUser() {
        List<User> userList = userRepository.findAllUser();

        if (userList.isEmpty()) {
            System.out.println("회원이 존재하지 않습니다");
        }
        return userList;
    }

    @Override
    public void update(UUID userUUID, String nickname) {
        User user = userRepository.updateUserNickname(userUUID, nickname);
        if (user == null) {
            System.out.println("[실패] 닉네임 변경을 실패하였습니다.");
            return;
        }

        System.out.println("[성공]" + user);

    }

    @Override
    public void delete(UUID uuid) {
        boolean isDeleted = userRepository.deleteUserById(uuid);
        if (!isDeleted) {
            System.out.println("삭제 실패");
            return;
        }
        System.out.println("[성공]");
    }
}
