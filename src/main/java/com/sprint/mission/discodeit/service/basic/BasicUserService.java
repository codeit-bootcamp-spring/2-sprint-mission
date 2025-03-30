package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.UserResponseDTO;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    // 다른 Service 대신 필요한 Repository 의존성 주입
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponseDTO create(CreateUserDTO createUserDTO) {
        // 동일한 username을 가진 사용자가 존재하는지 확인
        Optional<User> existingUser = userRepository.findByUsername(createUserDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("User already exists with username: " + createUserDTO.getUsername());
        }

        User user = new User(createUserDTO.getUsername(), createUserDTO.getEmail(), createUserDTO.getPassword());
        user = userRepository.save(user);

        UserStatus userStatus = new UserStatus(UUID.randomUUID(), user.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        BinaryContent profile = new BinaryContent(
                UUID.randomUUID(),
                createUserDTO.getImage(),
                createUserDTO.getImageContentType(),
                user.getId(),
                null
        );
        binaryContentRepository.save(profile);
        user = userRepository.save(user);

        // user에 관한 드러낼 수 있는 정보를 저장해놓은 DTO
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), userStatus.isLogin());
    }

    @Override
    public UserResponseDTO find(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("User with id " + userId + " not found"));
        UserStatus status = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(()->new NoSuchElementException("User with id " + userId + " not found"));
        boolean online = status != null && status.isLogin();
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), online);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            Optional<UserStatus> optionalStatus = userStatusRepository.findByUserId(user.getId());
            boolean online = optionalStatus.map(UserStatus::isLogin).orElse(false);
            // status != null && status.isLogin();
            return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), online);
        }).toList();
    }

    @Override
    public void update(UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(updateUserDTO.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + updateUserDTO.getUserId() + " not found"));
        user.update(user.getUsername(), user.getEmail(), user.getPassword());
        user = userRepository.save(user);

        if(updateUserDTO.getNewImage() != null) {
            BinaryContent profile = new BinaryContent(
                    null,
                    updateUserDTO.getNewImage(),
                    updateUserDTO.getNewImageContentType(),
                    user.getId(),
                    null
            );
            binaryContentRepository.save(profile);
            user.setProfileId(profile.getId());
            user = userRepository.save(user);
        }
        User finalUser = user;
        UserStatus status = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(()->new NoSuchElementException("User with id " + finalUser.getId() + " not found"));
        boolean online = status != null && status.isLogin();
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }

        BinaryContent profile = binaryContentRepository.findByUserId(userId)
                .orElseThrow(()-> new NoSuchElementException("BinaryContent with id " + userId + " not found"));
        if(profile != null) {
            binaryContentRepository.delete(profile);
        }
        UserStatus status = userStatusRepository.findByUserId(userId)
                .orElseThrow(()->new NoSuchElementException("User with id " + userId + " not found"));
        if(status != null) {
            userStatusRepository.delete(status);
        }
        userRepository.deleteById(userId);
    }
}
