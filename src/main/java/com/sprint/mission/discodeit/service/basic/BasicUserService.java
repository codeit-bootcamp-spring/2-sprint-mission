package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.response.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.response.UserUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserCreateResponse create(UserCreateRequest userCreateRequest) {
        // username과 email은 다른 유저와 같으면 안됩니다.
        validationUserCreateRequest(userCreateRequest);

        User user = new User(
                userCreateRequest.username(),
                userCreateRequest.email(),
                userCreateRequest.password()
        );

        // 등록할 프로필 이미지가 있다면 등록
        userCreateRequest.BinaryContentId().ifPresent(user::setProfileId);

        // UserStatus 를 같이 생성합니다.
        UserStatus userStatus = new UserStatus(user.getId(), Instant.now().minusSeconds(3 * 60));
        userStatusRepository.save(userStatus);
        userRepository.save(user);

        return new UserCreateResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Override
    public UserFindResponse find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(()-> new NoSuchElementException("User with id " + userId + " not found"));

        return new UserFindResponse(user.getUsername(), user.getEmail(), userStatus);
    }

    @Override
    public List<UserFindResponse> findAll() {
        return userRepository.findAll().stream()
                .map(u -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(u.getId())
                            .orElseThrow(()-> new NoSuchElementException("User with id " + u.getId() + " not found"));

                    return new UserFindResponse(u.getUsername(), u.getEmail(), userStatus);
                }).toList();
    }

    @Override
    public UserUpdateResponse update(UserUpdateRequest request) {
        User user = userRepository.findById(request.Id())
                .orElseThrow(() -> new NoSuchElementException("User with id " + request.Id() + " not found"));

        user.update(
                request.username().orElse(user.getUsername()),
                request.email().orElse(user.getEmail()),
                request.password().orElse(user.getPassword())
        );

        request.BinaryContentId().ifPresent(user::setProfileId);

        userRepository.save(user);

        return new UserUpdateResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                Optional.ofNullable(user.getProfileId()));
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }

        // 관련된 도메인도 같이 삭제합니다.
        // BinaryContent(프로필), UserStatus
        userRepository.findById(userId)
                .ifPresent(user -> binaryContentRepository.delete(user.getId()));
        userStatusRepository.findByUserId(userId)
                .ifPresent(status -> userStatusRepository.delete(status.getId()));

        userRepository.deleteById(userId);
    }

    private void validationUserCreateRequest(UserCreateRequest userCreateRequest) {
        // username과 email은 다른 유저와 같으면 안됩니다.
        boolean usernameExists = (userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(userCreateRequest.username())));

        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(userCreateRequest.email()));

        if(usernameExists) {
            throw new IllegalArgumentException("Username already exists");
        }

        if(emailExists){
            throw new IllegalArgumentException("Email already exists");
        }
    }
}
