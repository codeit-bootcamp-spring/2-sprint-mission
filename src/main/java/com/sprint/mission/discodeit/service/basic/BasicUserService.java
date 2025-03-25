package com.sprint.mission.discodeit.service.basic;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.UserCreateDto;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void create(UserCreateDto userCreateDto) {
        if (userRepository.findById(userCreateDto.id()).isPresent() ) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다: " + userCreateDto.id());
        }

        if (userRepository.findByUserName(userCreateDto.userName()).isPresent() ) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다: " + userCreateDto.id());
        }

        if (userRepository.findByEmail(userCreateDto.userEmail()).isPresent() ) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + userCreateDto.id());
        }

        String encodedPassword = passwordEncoder.encode(userCreateDto.password());

        UUID binaryID = UUID.randomUUID();

        BinaryContent binaryContent = new BinaryContent(
                binaryID,
                userCreateDto.id(),
                userCreateDto.uploadFileName(),
                userCreateDto.storeFileName()
        );

        User user = new User(
                userCreateDto.id(),
                Instant.now(),
                userCreateDto.userName(),
                encodedPassword,
                userCreateDto.userEmail(),
                binaryID
        );

        UserStatus userStatus = new UserStatus(UUID.randomUUID(), userCreateDto.id(), Boolean.FALSE);

        binaryContentRepository.save(binaryContent);
        userStatusRepository.save(userStatus);
        userRepository.save(user);
    }

    @Override
    public UserDto findById(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        UserStatus userStatus = userStatusRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("상태를 찾을 수 없습니다."));

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                userStatus.getOnlineStatus()
        );
    };

    @Override
    public List<UserDto> findAll() {
        // 모든 User를 조회
        List<User> users = userRepository.findAll()
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // User와 UserStatus를 결합하여 UserDto 생성
        return users.stream()
                .map(user -> {
                    // UserStatus 조회
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new IllegalArgumentException("사용자 상태를 찾을 수 없습니다. 사용자 ID: " + user.getId()));

                    // User와 UserStatus를 결합하여 UserDto 생성
                    return new UserDto(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            userStatus.getOnlineStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void update(UserUpdateDto userUpdateDto) {
        User user = userRepository
                .findById(userUpdateDto.id())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (userRepository.findByUserName(userUpdateDto.userName()).isPresent() ) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다: " + userUpdateDto.id());
        }

        if (userRepository.findByEmail(userUpdateDto.userEmail()).isPresent() ) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + userUpdateDto.userEmail());
        }

        user.userUpdate(Instant.now(), userUpdateDto.userName(), userUpdateDto.userEmail());
        userRepository.update(user);
    }

    @Override
    public void delete(UUID id) {
        this.findById(id);
        userRepository.delete(id);

        UserStatus userStatus = userStatusRepository.findByUserId(id).orElseThrow(() -> new IllegalArgumentException("사용자 상태를 찾을 수 없습니다."));

        userStatusRepository.delete(userStatus.getId());

        BinaryContent binaryContent = binaryContentRepository
                .findAllByIdIn(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자 상태를 찾을 수 없습니다."))
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용자 상태를 찾을 수 없습니다."));

        binaryContentRepository.delete(binaryContent.getId());
    }
}
