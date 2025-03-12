package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
    private final UserRepository repository;

    public FileUserService(String filename) {
        this.repository = new FileUserRepository(filename);
    }

    @Override
    public User create(String username, String password) {
        User user = new User(username, password);
        repository.save(user);
        System.out.println("사용자 생성 및 저장 완료: " + user.getId());
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return Optional.ofNullable(repository.findById(userId))
                .orElseThrow(() -> new NoSuchElementException(userId + "가 존재하지 않음."));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User updateName(UUID userId, String newUsername) {
        User user = findById(userId);
        user.updateName(newUsername); //데이터 상태 업데이트
        repository.save(user); //저장 공간의 객체 내용 업데이트
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
        System.out.println("사용자 삭제 및 저장 완료: " + userId);
    }
}
