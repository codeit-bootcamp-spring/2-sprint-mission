package com.sprint.discodeit.sprint4.service.jcf;

import com.sprint.discodeit.sprint5.domain.entity.User;
import com.sprint.discodeit.sprint4.repository.jcf.JCFUserRepository;
import com.sprint.discodeit.sprint4.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final JCFUserRepository jcfUserRepository;

    public JCFUserService(JCFUserRepository jcfUserRepository) {
        this.jcfUserRepository = jcfUserRepository;
    }


    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        jcfUserRepository.save(user);
        return user;
    }

    @Override
    public User find(UUID userId) {
      return jcfUserRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(userId.toString() + " 없는 회원 입니다"));
    }

    @Override
    public List<User> findAll() {
        return jcfUserRepository.findByAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = jcfUserRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(userId.toString() + " 없는 회원 입니다"));
        user.update(newUsername, newEmail, newPassword);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        jcfUserRepository.delete(userId);
    }
}
