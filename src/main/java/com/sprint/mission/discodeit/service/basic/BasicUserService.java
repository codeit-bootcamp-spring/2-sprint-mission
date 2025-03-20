package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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

    @Override
    public UserDto register(UserRegisterDto userRegisterDto, UUID profileId) {
        validateDuplicateEmail(userRegisterDto.email());
        validateDuplicateUserName(userRegisterDto.name());

        User requestUser = new User(
                userRegisterDto.name(),
                userRegisterDto.email(),
                userRegisterDto.password(),
                profileId);
        User savedUser = userRepository.save(requestUser);

        return UserDto.fromEntity(savedUser);
    }

    @Override
    public UserDto findById(UUID id) {
        User user = userRepository.findById(id)
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
    public void updateName(UUID id, String name) {
        userRepository.updateName(id, name);
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }

    private void validateDuplicateUserName(String name) {
        boolean isDuplicate = !userRepository.findByName(name)
                .isEmpty();

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
