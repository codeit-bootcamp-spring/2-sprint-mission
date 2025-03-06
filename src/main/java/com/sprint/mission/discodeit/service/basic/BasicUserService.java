package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String username, String email, String password) {
        boolean isExistUser = findAll().stream().anyMatch(user -> user.getUsername().equals(email)); // 동일한 이메일 == 같은 유저

        if (isExistUser) {
            throw new RuntimeException(email + " 이메일은 이미 가입되었습니다.");
        }

        User newUser = new User(username, email, password);

        return userRepository.save(newUser);
    }

    @Override
    public User findById(UUID userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new NoSuchElementException(userId + " 유저를 찾을 수 없습니다.");
        }

        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = findById(userId);
        user.update(newUsername, newEmail, newPassword);

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = findById(userId);
        userRepository.delete(user.getId());
    }
}
