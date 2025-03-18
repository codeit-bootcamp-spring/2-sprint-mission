package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ProfileImageRequest;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sprint.mission.discodeit.dto.UpdateDefinition;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User create(UserCreateRequest userRequest, ProfileImageRequest profileRequest){
        if(userRepository.findByUsername(userRequest.getUsername()).

    isPresent())

    {
        throw new IllegalArgumentException("Username already exists" + userRequest.getUsername());
    }
        if(userRepository.findByEmail(userRequest.getEmail()).

    isPresent())

    {
        throw new IllegalArgumentException("Email already exists:" + userRequest.getEmail());
    }

    User user = new User(userRequest.getUsername(), userRequest.getEmail(), userRequest.getPassword());
        userRepository.save(user);

        if(profileRequest !=null&&profileRequest.getImageData()!=null&&profileRequest.getImageData().length >0)

    {
        BinaryContent profileImage = new BinaryContent(profileRequest.getImageData(), user.getId(), null);
        binaryContentRepository.save(profileImage);
        user.updateProfileImage(profileImage.getId());
    }

    UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
    userStatusRepository.save(userStatus);

    return user;
}


    @Override
    public UserResponseDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id" + userId + " not found"));

        boolean isOnline = userStatusRepository.findByUserId(userId)
                .map(userStatus -> userStatus.isUserOnline())
                .orElse(false);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileId(user.getProfileId())
                .isOnline(isOnline)
                .build();
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    boolean isOnline = userStatusRepository.findByUserId(user.getId())
                            .map(userStatus -> userStatus.isUserOnline())
                            .orElse(false);

                    return UserResponseDto.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .profileId(user.getProfileId())
                            .isOnline(isOnline)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public User update(UUID userId, UpdateDefinition updateDefinition, UUID newProfileId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        user.update(updateDefinition, newProfileId);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }
}
