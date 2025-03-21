package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserDto userDto, Optional<BinaryContentDto> binaryContentDto) {
        String userName = userDto.userName();
        String userPassword = userDto.userPassword();
        String userEmail = userDto.userEmail();

        if (userRepository.existsByUserName(userDto.userName())) {
            throw new IllegalArgumentException("이미 사용중인 이름입니다.");
        }
        if (userRepository.existsByUserEmail(userDto.userEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        UUID profileId = binaryContentDto
                .map(this::createBinaryContent)
                .orElse(null);

        User user = new User(userName, userPassword, userEmail, profileId);
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId(), Instant.now(), true);
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public UserDto findById(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(userId)
                            .orElseThrow(() -> new NoSuchElementException("해당 사용자의 상태 정보를 찾을 수 없습니다."));

                    boolean isOnline = userStatus.isCurrentlyOnline();

                    return new UserDto(user.getId(), user.getUserName(), user.getUserEmail(), null, user.getProfileId(), isOnline);
                })
                .orElseThrow(() -> new NoSuchElementException("해당 사용자의 정보가 존재하지 않습니다."));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new NoSuchElementException("해당 사용자의 상태 정보를 찾을 수 없습니다."));

                    boolean isOnline = userStatus.isCurrentlyOnline();

                    return new UserDto(user.getId(), user.getUserName(), user.getUserEmail(), null, user.getProfileId(), isOnline);
                })
                .collect(Collectors.toList());
    }

    @Override
    public User update(UUID userId, UserUpdateDto userUpdateDto, Optional<BinaryContentDto> userProfile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));

        String newUsername = userUpdateDto.newUsername();
        String newEmail = userUpdateDto.newEmail();
        String newPassword = userUpdateDto.newPassword();

        if (userRepository.existsByUserEmail(newEmail) && !newEmail.equals(user.getUserEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        if (userRepository.existsByUserName(newUsername) && !newUsername.equals(user.getUserName())) {
            throw new IllegalArgumentException("이미 사용중인 사용자 이름입니다.");
        }

        UUID newProfileId = userProfile
                .map(this::createBinaryContent)
                .orElse(user.getProfileId());

        user.update(newUsername, newEmail, newPassword, newProfileId);
        userRepository.save(user);

        return user;
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));

        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }

        userStatusRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }

    private UUID createBinaryContent(BinaryContentDto profileImage) {
        String fileName = profileImage.fileName();
        String contentType = profileImage.contentType();
        byte[] fileData = profileImage.fileData();
        Long fileSize = (long) fileData.length;

        BinaryContent binaryContent = new BinaryContent(fileName, fileData, fileSize, contentType);
        return binaryContentRepository.save(binaryContent).getId();
    }
}