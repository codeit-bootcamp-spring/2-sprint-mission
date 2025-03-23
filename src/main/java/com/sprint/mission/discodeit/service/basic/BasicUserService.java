package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.username()) || userRepository.existsByEmail(userDTO.email())) {
            throw new IllegalArgumentException("이미 존재하는 username 또는 email입니다.");
        }

        UserStatus userStatus = new UserStatus(Instant.now());
        User user = new User(userDTO.username(), userDTO.email(), userStatus);  // This now uses the constructor correctly

        if (userDTO.profileImage() != null) {
            BinaryContent profileImage = new BinaryContent(userDTO.profileImage());
            binaryContentRepository.save(profileImage);
            user.setProfileImage(profileImage);
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID id, String newUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        user.updateUsername(newUsername);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        getUser(id);
        userRepository.delete(id);
    }
}

