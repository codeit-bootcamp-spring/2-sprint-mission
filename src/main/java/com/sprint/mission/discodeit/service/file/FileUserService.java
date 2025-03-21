package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class FileUserService implements UserService {
    private static FileUserService instance;
    private final FileUserRepository fileUserRepository;

    public FileUserService(FileUserRepository fileUserRepository) {
        this.fileUserRepository = fileUserRepository;
    }

    public static synchronized FileUserService getInstance(FileUserRepository fileUserRepository) {
        if (instance == null) {
            instance = new FileUserService(fileUserRepository);
        }
        return instance;
    }

    @Override
    public User create(String userName, String userEmail, String userPassword) {
        User user = new User(userName, userEmail, userPassword);
        fileUserRepository.save(user);
        return user;
    }

    @Override
    public List<UserDto> findAll() {
        return fileUserRepository.findAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public User findById(UUID userId) {
        return fileUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User existingUser = fileUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
        existingUser.update(newUsername, newEmail, newPassword);
        fileUserRepository.save(existingUser);
        return existingUser;
    }


    @Override
    public void delete(UUID userId) {
        if (!fileUserRepository.delete(userId)) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다 : " + userId);
        }
    }
}
