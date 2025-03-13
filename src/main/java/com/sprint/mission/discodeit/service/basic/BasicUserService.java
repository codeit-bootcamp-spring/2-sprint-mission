package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private static BasicUserService instance; // 싱글톤 인스턴스
    private final UserRepository userRepository; // 의존성 주입

    private BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static synchronized BasicUserService getInstance(UserRepository userRepository) {
        if (instance == null) {
            instance = new BasicUserService(userRepository);
        }
        return instance;
    }

    @Override
    public UUID createUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 사용자 이름입니다: " + username);
        }
        User user = new User(username);
        userRepository.save(user);
        System.out.println("사용자가 생성되었습니다: " + user);
        return user.getId();
    }

    @Override
    public void searchUser(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            System.out.println("조회하신 사용자가 존재하지 않습니다.");
            return;
        }
        System.out.println("USER: " + user);
    }

    @Override
    public void searchAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("등록된 사용자가 없습니다.");
            return;
        }
        users.forEach(user -> System.out.println("USER: " + user));
    }

    @Override
    public void updateUser(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            System.out.println("업데이트할 사용자가 존재하지 않습니다.");
            return;
        }
        user.updateTime(System.currentTimeMillis());
        userRepository.update(user);
        System.out.println(id + " 사용자 업데이트 완료되었습니다.");
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            System.out.println("삭제할 사용자가 존재하지 않습니다.");
            return;
        }
        userRepository.delete(id);
        System.out.println(id + " 사용자 삭제 완료되었습니다.");
    }
}
