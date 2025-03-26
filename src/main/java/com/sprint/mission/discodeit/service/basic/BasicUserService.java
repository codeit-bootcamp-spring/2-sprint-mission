package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UpdateUserReqDto;
import com.sprint.mission.discodeit.dto.user.CreateUserReqDto;
import com.sprint.mission.discodeit.dto.usertstatus.UserStatusResDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.dto.user.UserResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public UserResDto create(CreateUserReqDto createUserReqDto) {
        if (userRepository.existsByUsername(createUserReqDto.username())) {
            throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        }
        if (userRepository.existsByEmail(createUserReqDto.email())) {
            throw new IllegalArgumentException("이미 존재하는 email 입니다.");
        }

        UUID profileId = null;
        if (createUserReqDto.profileFile() != null && createUserReqDto.profileFile().length > 0) {
            BinaryContent binaryContent = new BinaryContent(createUserReqDto.profileFile());
            binaryContentRepository.save(binaryContent);
            profileId = binaryContent.getId();
        }

        User user = new User(createUserReqDto.username(), createUserReqDto.email(), createUserReqDto.password(), profileId);
        userRepository.save(user);

        userStatusRepository.findByUserId(user.getId())
                .ifPresent(userStatus -> {
                    throw new IllegalArgumentException("관련된 userStatus 객체가 이미 존재합니다.");
                });

        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        return new UserResDto(user.getId(), user.getUsername(), user.getEmail(), user.getProfileId(), new UserStatusResDto(userStatus.getId(), userStatus.getUserId(), userStatus.isOnline()));
    }

    @Override
    public UserResDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElseThrow(() -> new NoSuchElementException("UserStatus with id " + user.getId() + " not found"));

        return new UserResDto(user.getId(), user.getUsername(), user.getEmail(), user.getProfileId(), new UserStatusResDto(userStatus.getId(), userStatus.getUserId(), userStatus.isOnline()));
    }

    @Override
    public List<UserResDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + user.getId() + " not found"));
                    return new UserResDto(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getProfileId(),
                            new UserStatusResDto(userStatus.getId(), userStatus.getUserId(), userStatus.isOnline())
                    );
                })
                .toList();
    }

    @Override
    public UserResDto update(UUID userId, UpdateUserReqDto updateUserReqDto) {
        if (updateUserReqDto.username() == null || updateUserReqDto.username().trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임은 null이거나 빈 값일 수 없습니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 user 객체를 찾을 수 없습니다."));

        UUID profileId = null;
        if (updateUserReqDto.profileFile() != null && updateUserReqDto.profileFile().length > 0) {
            BinaryContent binaryContent = new BinaryContent(updateUserReqDto.profileFile());
            binaryContentRepository.save(binaryContent);
            profileId = binaryContent.getId();
        }

        user.updateUser(updateUserReqDto.username(), updateUserReqDto.email(), updateUserReqDto.password(), profileId, Instant.now());

        User updatedUser = userRepository.save(user);
        UserStatus userStatus = userStatusRepository.findByUserId(updatedUser.getId()).orElseThrow(() -> new NoSuchElementException("UserStatus with id " + updatedUser.getId() + " not found"));
        return new UserResDto(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getProfileId(), new UserStatusResDto(userStatus.getId(), userStatus.getUserId(), userStatus.isOnline()));

    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 user 객체를 찾을 수 없습니다."));

        userStatusRepository.deleteByUserId(userId);

        if(user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }

        userRepository.deleteById(userId);
    }
}
