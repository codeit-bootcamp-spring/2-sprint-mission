package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    BinaryContentRepository BinaryContentRepository;
    UserStatusRepository UserStatusRepository;

    @Override
    public UUID create(UserCreateRequest request) {
        // username과 email의 중복 검사
        // 모든 데이터를 찾아서 그곳에 username, 혹은 email 중복이 있는지 체크
        for (User user : userRepository.findAll()) {
            if (user.getUsername().equals(request.username())) {
                throw new IllegalArgumentException("The username already exists.");
            }
            if (user.getEmail().equals(request.email())) {
                throw new IllegalArgumentException("This email already exists.");
            }
        }

        UUID profileImage =saveProfileImage(request.profileImage());
        UserStatusRepository.upsert(request.status());

        // User 생성
        User user = new User(
                request.username(),
                request.email(),
                request.password(),
                profileImage
        );
        userRepository.upsert(user);



        return user.getId();
    }

    @Override
    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NoSuchElementException("Could not find user with that ID. : " + id);
        }
        boolean isOnline = UserStatusRepository.isUserOnline(id);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), isOnline);
    }


    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail(), UserStatusRepository.isUserOnline(user.getId())))
                .toList();
    }

    @Override
    public void update(UserUpdateRequest request) {
        // 기존 유저 정보 조회
        User user = userRepository.findById(request.userId());
        if (user == null) {
            throw new NoSuchElementException("Could not find user with that ID. : " + request.userId());
        }

        // 사용자 정보 업데이트 (이름 & 이메일)
        userRepository.update(request.userId(), request.username(), request.email(), request.password(), null);

        saveProfileImage(request.profileImage());
    }

    @Override
    public void delete(UUID id) {
        // 사용자 존재 여부 확인
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NoSuchElementException("Could not find user with that ID. : " + id);
        }

        // 관련 데이터 삭제 (프로필 이미지, 유저 상태)
        BinaryContentRepository.deleteProfileImageByUserId(id);
        UserStatusRepository.deleteByUserId(id);
        // 최종적으로 사용자 삭제
        userRepository.delete(id);
    }

    private UUID saveProfileImage(byte[] profileImage) {
        if (profileImage == null || profileImage.length == 0) {
            throw new IllegalArgumentException("Profile image cannot be null or empty");
        }
        return BinaryContentRepository.upsert(new BinaryContent(profileImage, "image/png", profileImage.length));
    }
}
