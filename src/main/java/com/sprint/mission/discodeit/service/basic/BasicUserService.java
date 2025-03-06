package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {

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
    public Optional<List<User>> findAllUser() {
        List<User> userList = userRepository.findAllUser();

        if (userList.isEmpty()) {
            System.out.println("회원이 존재하지 않습니다");
            return Optional.empty();
        }
        return Optional.of(userList);
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
