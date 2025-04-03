package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
    public UserDto create(UserCreateRequest request, Optional<BinaryContentCreateRequest> optionalBinaryContentCreateRequest) {
        // username과 email은 다른 유저와 같으면 안됩니다.
        validationUserCreateRequest(request);

        UUID nullableProfileId = optionalBinaryContentCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    binaryContentRepository.save(binaryContent);
                    return binaryContent.getId();
                })
                .orElse(null);

        User user = new User(
                request.username(),
                request.email(),
                request.password(),
                nullableProfileId
        );

        // UserStatus 를 같이 생성합니다.
        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.save(userStatus);
        userRepository.save(user);

        return toDto(user);
    }

    @Override
    public UserDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        return toDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                            .orElseThrow(()-> new NoSuchElementException("User with id " + user.getId() + " not found"));

                    return toDto(user);
                }).toList();
    }

    @Override
    public UserDto update(UserUpdateRequest request, Optional<BinaryContentCreateRequest> optionalBinaryContentRequest) {
        User user = userRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("User with id " + request.id() + " not found"));

        user.update(
                request.username().orElse(user.getUsername()),
                request.email().orElse(user.getEmail()),
                request.password().orElse(user.getPassword())
        );

        // 업데이트 할 바이너리 컨텐츠가 있으면 교체
        optionalBinaryContentRequest.ifPresent(binaryContentRequest ->{
            BinaryContent binaryContent = new BinaryContent(
                    binaryContentRequest.fileName(),
                    (long)binaryContentRequest.bytes().length,
                    binaryContentRequest.contentType(),
                    binaryContentRequest.bytes()
            );
            binaryContentRepository.save(binaryContent);
            user.setProfileId(binaryContent.getId());
        });

        userRepository.save(user);

        return toDto(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }

        // 관련된 도메인도 같이 삭제합니다.
        // BinaryContent(프로필), UserStatus
        userRepository.findById(userId)
                .ifPresent(user ->{
                            if(user.getProfileId() != null) {
                                binaryContentRepository.delete(user.getProfileId());
                            }
                        });
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

    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }
}
