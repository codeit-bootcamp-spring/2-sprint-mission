package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserCreateResponse create(UserCreateRequest requestDto) {
        validateUsernameDuplicate(requestDto.username());
        validateEmailDuplicate(requestDto.email());

        User user = new User(requestDto.username(),
                requestDto.email(), requestDto.password(), requestDto.profileId());
        userRepository.save(user);

        userStatusService.create(new UserStatusCreateRequest(user.getId()));

        return UserCreateResponse.fromEntity(user);
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = getUserBy(userId);

        return UserResponse.fromEntity(user, isOnline(userId));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserResponse.fromEntity(user, isOnline(user.getId())))
                .toList();
    }

    @Override
    public UserUpdateResponse update(UserUpdateRequest requestDto) {
        User user = getUserBy(requestDto.id());

        if (!user.getUsername().equals(requestDto.username())) {
            validateUsernameDuplicate(requestDto.username());
        }

        if (!user.getEmail().equals(requestDto.email())) {
            validateEmailDuplicate(requestDto.email());
        }

        user.update(requestDto.username(), requestDto.email(),
                 requestDto.password(), requestDto.profileId());
        userRepository.save(user);

        return UserUpdateResponse.fromEntity(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = getUserBy(userId);

        if(user.hasProfile()) {
            binaryContentRepository.deleteById(user.getProfileId());
        }

        userStatusService.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }

    private User getUserBy(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저 없음"));
    }


    private void validateEmailDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("동일 email 이미 존재함");
        }
    }

    private void validateUsernameDuplicate(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("동일 username 이미 존재함");
        }
    }

    private boolean isOnline(UUID userId) {
        return userStatusService.findByUserId(userId).isOnline();
    }
}
