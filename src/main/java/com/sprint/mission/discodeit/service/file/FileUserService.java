package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

public class FileUserService implements UserService {
    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto register(UserRegisterDto userRegisterDto, UUID profileId) {
        User requestUser = new User(userRegisterDto.name(), userRegisterDto.email(), userRegisterDto.password(), null);
        validateDuplicateEmail(requestUser);
        User savedUser = userRepository.save(requestUser);

        return UserDto.fromEntity(savedUser);
    }

    @Override
    public UserDto findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto findByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        return UserDto.fromEntity(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .toList();
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));

        return UserDto.fromEntity(user);
    }

    @Override
    public List<UserDto> findAllByIds(List<UUID> userIds) {
        return userIds
                .stream()
                .map(this::findById)
                .toList();
    }

    @Override
    public void updateName(UUID userId, String name) {
        userRepository.updateName(userId, name);
    }

    @Override
    public UserDto updateProfileImage(UUID userId, UUID profileId) {
        return null;
    }

    @Override
    public void delete(UUID userId) {
        userRepository.delete(userId);
    }

    private void validateDuplicateEmail(User requestUser) {
        boolean isDuplicate = userRepository.findAll()
                .stream()
                .anyMatch(existingUser -> existingUser.isSameEmail(requestUser.getEmail()));

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다");
        }
    }
}
