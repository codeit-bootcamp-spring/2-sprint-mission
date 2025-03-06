package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final List<User> userList = new ArrayList<>();
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(String nickname, String password) {
        User user = userRepository.userSave(nickname, password);

        if (user == null) {
            System.out.println("[실패] 저장 실패.");
            return;
        }

        userList.add(user);

        System.out.println("[성공]" + user);
    }

    @Override
    public User findByUser(UUID userUUID) {
        return userList.stream()
                .filter(user -> user.getId().equals(userUUID))
                .findAny()
                .orElseGet(() -> {
                    System.out.println("[실패] 사용자가 존재하지 않습니다.");
                    return null;
                });
    }

    @Override
    public Optional<List<User>> findAllUser() {
        if (userList.isEmpty()) {
            System.out.println("아이디가 존재하지 않습니다");
            return Optional.empty();
        }
        return Optional.of(userList);
    }

    @Override
    public void update(UUID userUUID, String nickname) {
        userList.stream()
                .filter(user -> user.getId().equals(userUUID))
                .findAny()
                .ifPresentOrElse(
                        (user) -> {
                            user.updateNickname(nickname);
                            System.out.println("[성공] 사용자 변경 완료");
                        },
                        () -> {
                            System.out.println("[실패] 수정에 실패하였습니다.");
                        }
                );
    }

    @Override
    public void delete(UUID uuid) {
        boolean removed = userList.removeIf(user -> user.getId().equals(uuid));
        if (!removed) {
            System.out.println("[실패]존재하지 않는 사용자");
        } else {
            System.out.println("[성공]사용자 삭제 완료");
        }
    }

    @Override
    public UUID login(UUID userUUID, String password) {
        User userInfo = findByUser(userUUID);
        if (userInfo == null) {
            return null;
        }

        if (!userInfo.getPassword().equals(password)) {
            System.out.println("[실패]비밀번호가 일치하지 않습니다.");
            return null;
        }

        System.out.println("[성공] 로그인 완료");
        return userInfo.getId();
    }
}
