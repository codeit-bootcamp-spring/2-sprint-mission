package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.userDto.UserRequest;
import com.sprint.mission.discodeit.service.userDto.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.userDto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;


    public UserResponse create(UserRequest request) {

        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new IllegalArgumentException("Username or Email already exists");
        } else{
            User user = new User(request.username(), request.email(), request.password());
            userRepository.save(user);
        }


        User user = new User(request.username(), request.email(), request.password());
        UserStatus status = new UserStatus(user, Instant.now());
        userStatusRepository.save(status);

        try{
            if (request.profileImage() != null) {
                BinaryContent profileImage = new BinaryContent(user.getId(), request.profileImage());
                binaryContentRepository.save(profileImage);
            }
            else throw new IllegalArgumentException("Profile image is required");
        }
        catch (Exception e1){
            System.out.println("Error creating user profile image");
        }


        return new UserResponse(user.getId(),request.username(),request.email(),
                status.isOnline());
    }

    @Override
    public UserResponse find(UUID userId) {

        UserStatus status = userStatusRepository.findByUserId(userId);
        if(status.isOnline()){
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return new UserResponse(userId, user.getUsername(),
                    user.getEmail(), status.isOnline());
        }else {
            System.out.println("User not IsOnline");
            return null;
        }


    }

    public List<UserResponse> findAll() {
        return userStatusRepository.findAll();
    }

    public void delete(UUID userId) {
        // 유저 존재 확인
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        // 관련 데이터 삭제
        binaryContentRepository.deleteById(userId); // 프로필 이미지 삭제
        userStatusRepository.deleteByUserId(userId); // 유저 상태 삭제
        userRepository.deleteById(userId); // 유저 삭제
    }


    public UserResponse update(UserUpdateRequest request) {
        User user = userRepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // username, email 중복 체크
        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new IllegalArgumentException("Username or Email already exists");
        }

        // 변경할 값 적용
        if (request.username() != null) {
            user.setUsername(request.username());
        }
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        userRepository.save(user);

        // 프로필 이미지 변경 처리
        if (request.profileImage() != null) {
            binaryContentRepository.deleteById(user.getId()); // 기존 이미지 삭제
            try {
                BinaryContent newProfileImage = new BinaryContent(
                        user.getId(),
                        request.profileImage().getBytes()
                );
                binaryContentRepository.save(newProfileImage);
            } catch (IOException e) {
                throw new RuntimeException("Failed to process profile image", e);
            }

        }

        UserStatus status = userStatusRepository.findByUserId(user.getId());

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
                status.isOnline());
    }

}
