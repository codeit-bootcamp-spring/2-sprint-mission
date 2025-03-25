package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserDto;
import com.sprint.mission.discodeit.dto.UpdateUserDto;
import com.sprint.mission.discodeit.dto.UserInfoDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    private void saveUser() {
        userRepository.save();
    }

    @Override
    public User createUser(CreateUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 Email입니다");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 Username입니다");
        }

        User user = new User(dto.getUsername(), dto.getEmail(), dto.getPassword());
        userRepository.addUser(user);
        userStatusRepository.addUserStatus(new UserStatus(user.getId()));

        String filePath = dto.getProfilePicturePath();
        if (filePath != null) {
            BinaryContent profile = new BinaryContent(user.getId(), filePath);
            binaryContentRepository.addBinaryContent(profile);
        }
        return user;
    }

    @Override
    public UserInfoDto getUserById(UUID userId) {
        return mapToUserFindDto(userRepository.findUserById(userId));
    }

    @Override
    public String getUserNameById(UUID userId) {
        return userRepository.findUserById(userId).getUsername();
    }

    @Override
    public List<UserInfoDto> findUsersByIds(Set<UUID> userIds) {
        return userRepository.findUsersByIds(userIds).stream()
                .map(this::mapToUserFindDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserInfoDto> getAllUsers() {
        return userRepository.findUserAll().stream()
                .map(this::mapToUserFindDto)
                .collect(Collectors.toList());
    }

    @Override
    public BinaryContent findProfileById(UUID userId) {
        return Optional.ofNullable(userRepository.findUserById(userId))
                .map(User::getProfileId)
                .map(binaryContentRepository::findBinaryContentById)
                .orElse(null);
    }

    @Override
    public void updateUser(UpdateUserDto dto) {
        validateUserExists(dto.getUserid());
        User user = userRepository.findUserById(dto.getUserid());

        if (dto.getUsername() != null) user.updateUsername(dto.getUsername());
        if (dto.getPassword() != null) user.updatePassword(dto.getPassword());
        if (dto.getEmail() != null) user.updateEmail(dto.getEmail());
        String filePath = dto.getProfilePicturePath();
        if (filePath != null) {
            BinaryContent profile = new BinaryContent(user.getId(), filePath);
            binaryContentRepository.addBinaryContent(profile);
        }
    }

    @Override
    public void addChannel(UUID userID, UUID channelId) {
        User user = userRepository.findUserById(userID);
        user.addJoinedChannel(channelId);
        saveUser();
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteUserById(userId);
        userStatusRepository.deleteUserStatusById(userId);
        binaryContentRepository.deleteBinaryContent(userId);
    }

    @Override
    public void removeChannel(UUID userId, UUID channelId) {
        User user = userRepository.findUserById(userId);
        user.removeJoinedChannel(channelId);
        saveUser();
    }

    @Override
    public void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("존재하지 않는 유저입니다.");
        }
    }

    private UserInfoDto mapToUserFindDto(User user) {
        UserInfoDto dto = new UserInfoDto();
        dto.setUserid(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(userStatusRepository.findUserStatusById(user.getId()).getStatus()); // ✅ 온라인 상태 추가
        return dto;
    }


}
