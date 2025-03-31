package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.UserResponseDto;
import com.sprint.mission.discodeit.service.dto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User createUser(UserCreateDto createDto) {
        checkUserEmailExists(createDto.email());
        checkUserNicknameExists(createDto.nickname());

        UUID profileId = null;
        BinaryContent binaryContent;
        if(createDto.profileImage() != null) {
            try {
                Path directory = Paths.get(System.getProperty("user.dir"), "data", "binaryContent");
                String filePath = directory.resolve(createDto.profileImage().getOriginalFilename()).toString();
                binaryContent = new BinaryContent(
                        createDto.profileImage().getBytes(),
                        filePath,
                        createDto.profileImage().getOriginalFilename(),
                        createDto.profileImage().getContentType(),
                        createDto.profileImage().getSize()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
            profileId = createdBinaryContent.getId();
        }

        User user = userRepository.save(createDto.convertDtoToUser(profileId));
        userStatusRepository.save(new UserStatus(user.getId()));
        return user;
    }

    @Override
    public UserResponseDto findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + id));
        boolean isOnline = checkUserOnlineStatus(id);

        return UserResponseDto.convertToResponseDto(user, isOnline);
    }

    @Override
    public UserResponseDto findByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElse(null);

        if (user == null) return null;

        boolean isOnline = checkUserOnlineStatus(user.getId());

        return UserResponseDto.convertToResponseDto(user, isOnline);
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) return null;

        boolean isOnline = checkUserOnlineStatus(user.getId());

        return UserResponseDto.convertToResponseDto(user, isOnline);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    boolean isOnline = checkUserOnlineStatus(user.getId());
                    return UserDto.convertToDto(user, isOnline);
                })
                .collect(Collectors.toList());
    }

    @Override
    public User updateUser(UserUpdateDto updateDto) {
        User user = userRepository.findById(updateDto.id())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + updateDto.id()));
        checkUserNicknameExists(updateDto.nickname());

        UUID profileId = null;
        BinaryContent binaryContent;
        if (updateDto.profileImage() != null) {
            if (updateDto.profileId() != null) {
                binaryContentRepository.deleteById(updateDto.profileId());
            }
            try {
                Path directory = Paths.get(System.getProperty("user.dir"), "data", "binaryContent");
                String filePath = directory.resolve(updateDto.profileImage().getOriginalFilename()).toString();
                binaryContent = new BinaryContent(
                        updateDto.profileImage().getBytes(),
                        filePath,
                        updateDto.profileImage().getOriginalFilename(),
                        updateDto.profileImage().getContentType(),
                        updateDto.profileImage().getSize()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
            profileId = createdBinaryContent.getId();
        }

        user.update(updateDto.password(), updateDto.nickname(), updateDto.status(), updateDto.role(), profileId);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + id));

        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + id));
        userStatusRepository.deleteById(userStatus.getId());

        userRepository.deleteById(id);
    }


    /****************************
     * Validation check
     ****************************/
    private boolean checkUserOnlineStatus(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId));
        return userStatus.isOnline();
    }

    private void checkUserEmailExists(String email) {
        if (findByEmail(email) != null) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }

    private void checkUserNicknameExists(String nickname) {
        if (findByNickname(nickname) != null) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
    }

}
