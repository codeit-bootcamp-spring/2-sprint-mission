package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.userdto.UserDto;
import com.sprint.mission.discodeit.application.userdto.UserRegisterDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto, UUID profileId) {
        validateDuplicateEmail(userRegisterDto.email());
        validateDuplicateUserName(userRegisterDto.name());

        User savedUser = userRepository.save(new User(
                userRegisterDto.name(),
                userRegisterDto.email(),
                userRegisterDto.password(),
                profileId)
        );
        userStatusRepository.save(savedUser.getUserStatus());

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        user.updateProfileImage(profileId);
        User savedUser = userRepository.save(user);

        return UserDto.fromEntity(savedUser);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent() + 11));

        userRepository.delete(userId);
        userStatusRepository.delete(user.getUserStatus().getId());
    }

    private void validateDuplicateUserName(String name) {
        boolean isDuplicate = userRepository.findByName(name)
                .isPresent();

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 이름 입니다");
        }
    }

    private void validateDuplicateEmail(String requestEmail) {
        boolean isDuplicate = userRepository.findAll()
                .stream()
                .anyMatch(existingUser -> existingUser.isSameEmail(requestEmail));

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
    }
}
