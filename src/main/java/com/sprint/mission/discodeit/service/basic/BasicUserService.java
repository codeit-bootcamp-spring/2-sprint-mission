package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserCreateResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        validateUserCreateRequestDto(userCreateRequestDto);

        User user = new User(userCreateRequestDto.username(),
                userCreateRequestDto.email(), userCreateRequestDto.password(), userCreateRequestDto.profileId());
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return UserCreateResponseDto.fromEntity(user);
    }

    private void validateUserCreateRequestDto(UserCreateRequestDto userCreateRequestDto) {
        if (userRepository.existsByUsername(userCreateRequestDto.username())) {
            throw new IllegalArgumentException("동일 username 이미 존재함");
        }
        if (userRepository.existsByEmail(userCreateRequestDto.email())) {
            throw new IllegalArgumentException("동일 email 이미 존재함");
        }
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = getUserBy(userId);

        return UserResponseDto.fromEntity(user, isOnline(userId));
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserResponseDto.fromEntity(user, isOnline(user.getId())))
                .toList();
    }

    private boolean isOnline(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 userId의 userStatus가 없음"))
                .isOnline();
    }

    @Override
    public UserUpdateResponseDto update(UserUpdateRequestDto userUpdateRequestDto) {
        User user = getUserBy(userUpdateRequestDto.id());
        user.update(userUpdateRequestDto.username(), userUpdateRequestDto.email(),
                 userUpdateRequestDto.password(), userUpdateRequestDto.profileId());
        userRepository.save(user);

        return UserUpdateResponseDto.fromEntity(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = getUserBy(userId);

        if(user.hasProfile()) {
            binaryContentRepository.deleteById(user.getProfileId());
        }

        userStatusRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }

    private User getUserBy(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저 없음"));
    }
}
