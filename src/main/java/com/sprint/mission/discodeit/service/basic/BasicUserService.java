package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private static BasicUserService instance;
    private final UserRepository userRepository;

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
    public void create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("유저 정보가 NULL입니다.");
        }

        if (userRepository.find(user.getId()) != null) {
            throw new RuntimeException("유저가 이미 존재합니다.");
        }
        userRepository.create(user);
        System.out.println("[" + user +"] 생성을 완료하였습니다. " + user.getId());
    }

    @Override
    public User find(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID가 NULL입니다.");
        }

        if (userRepository.find(id) == null) {
            throw new RuntimeException("유저가 존재하지 않습니다.");
        }
        System.out.println("선택한 유저를 조회합니다.");
        return userRepository.find(id);
    }

    @Override
    public List<User> findAll() {
        if (userRepository.findAll().isEmpty()) {
            System.out.println("등록된 유저가 없습니다.");
        }
        System.out.println("모든 유저를 조회합니다.");
        return userRepository.findAll();
    }

    @Override
    public void update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("유저 정보가 NULL입니다.");
        }

        if (userRepository.find(user.getId()) == null) {
            throw new RuntimeException("유저가 존재하지 않습니다.");
        }
        userRepository.update(user);
        System.out.println("[" + user +"] 수정을 완료하였습니다. " + user.getId());
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID가 NULL입니다.");
        }

        if (userRepository.find(id) == null) {
            throw new RuntimeException("유저가 존재하지 않습니다.");
        }
        userRepository.delete(id);
        System.out.println("유저 삭제를 완료하였습니다.");
    }
}
