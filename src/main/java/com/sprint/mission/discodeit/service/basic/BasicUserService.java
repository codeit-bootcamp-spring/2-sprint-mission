package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {
    private static volatile BasicUserService instance;
    private final UserRepository userRepository;

    private BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static BasicUserService getInstance(UserRepository userRepository) {
        if (instance == null) {
            synchronized (BasicUserService.class) {
                if (instance == null) {
                    instance = new BasicUserService(userRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public void create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User 객체가 null 입니다.");
        }
        userRepository.save(user);
    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(UUID userId) {
        User user = findById(userId);
        userRepository.delete(user.getId());
    }

    @Override
    public void update(UUID id, String nickname, String email, String password) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임은 null이거나 빈 값일 수 없습니다.");
        }

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.update(id, nickname, email, password);  // Repository의 update 메서드 호출
        } else {
            throw new NoSuchElementException("User not found with id: " + id);
        }
    }
}
