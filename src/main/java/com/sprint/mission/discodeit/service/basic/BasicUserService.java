package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.file.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User createUser(CreateUserRequest request,
        Optional<CreateBinaryContentRequest> profileOpt) {
        if (userRepository.findAll().stream().anyMatch(
            user -> user.getUsername().equals(request.username()) || user.getEmail()
                .equals(request.email())
        )) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        BinaryContent profile = profileOpt.map(p -> {
            BinaryContent binaryContent = new BinaryContent(
                p.fileName(),
                (long) p.bytes().length,
                p.contentType()
            );
            return binaryContentRepository.save(binaryContent);
        }).orElse(null);

        User user = new User(
            request.username(),
            request.password(),
            request.email(),
            profile
        );
        userRepository.save(user);
        userStatusRepository.save(new UserStatus(user));

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(UUID userId) {
        return userRepository.findById(userId)
            .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByName(String name) {
        return userRepository.findAll().stream()
            .filter(user -> user.getUsername().equals(name))
            .map(userMapper::toDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserRequest request,
        Optional<CreateBinaryContentRequest> profileOpt) {
        userRepository.findById(request.userId()).ifPresent(user -> {
            BinaryContent newProfile = profileOpt.map(profile -> {
                Optional.ofNullable(user.getProfile()).ifPresent(binaryContentRepository::delete);
                BinaryContent binaryContent = new BinaryContent(
                    profile.fileName(),
                    (long) profile.bytes().length,
                    profile.contentType()
                );
                return binaryContentRepository.save(binaryContent);
            }).orElse(user.getProfile());

            user.update(
                request.newUsername(),
                request.newPassword(),
                request.newEmail(),
                newProfile
            );
//            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.getProfile() != null) {
                binaryContentRepository.delete(user.getProfile());
            }
            userRepository.delete(user);
        });
    }
}
