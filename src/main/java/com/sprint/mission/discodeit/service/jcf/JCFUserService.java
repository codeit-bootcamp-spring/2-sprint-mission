package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository repository;

    public JCFUserService() {
        this.repository = new JCFUserRepository();
    }

    @Override
    public User create(String username, String password) {
        User user = new User(username, password);
        repository.save(user);
        System.out.println("사용자 생성 완료: " + user.getId());
        return user;
    }

    @Override
    public User findById(UUID userId) {
        System.out.println("사용자 조회: " + userId);
        return repository.findById(userId);
    }

    @Override
    public List<User> findAll() {
        System.out.println("모든 사용자 조회");
        return repository.findAll();
    }

    @Override
    public User updateName(UUID userId, String newUsername) {
        User user = repository.findById(userId);
        user.updateName(newUsername);
        repository.save(user);
        System.out.println("사용자 이름 변경 완료: " + newUsername);
        return user;
    }

    @Override
    public User updatePassword(UUID userId, String newPassword) {
        User user = findById(userId);
        user.updatePassword(newPassword);
        repository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        repository.delete(userId);
        System.out.println("사용자 삭제 완료: " + userId);
    }
}
