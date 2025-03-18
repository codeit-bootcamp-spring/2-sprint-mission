package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.dto.UserNameStatusResponse;
import com.sprint.discodeit.domain.dto.UserProfileImgResponseDto;
import com.sprint.discodeit.domain.dto.UserResponse;
import com.sprint.discodeit.domain.entity.BinaryContent;
import com.sprint.discodeit.domain.entity.StatusType;
import com.sprint.discodeit.domain.entity.UserStatus;
import com.sprint.discodeit.domain.mapper.UserMapper;
import com.sprint.discodeit.domain.dto.UserRequestDto;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.repository.UserStatusRepository;
import com.sprint.discodeit.repository.file.BaseUserStatusRepository;
import com.sprint.discodeit.repository.file.FileUserRepository;
import com.sprint.discodeit.service.UserServiceV1;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUserService implements UserServiceV1 {

    private final FileUserRepository fileUserRepository;
    private final BinaryServiceImpl binaryServiceImpl;
    private final UserStatusService userStatusService;
    private final BaseUserStatusRepository baseUserStatusRepository;

    public UserNameStatusResponse create(UserRequestDto userRequestDto, UserProfileImgResponseDto userProfileImgResponseDto) {
        // 중복된 유저 확인
        if (fileUserRepository.findByUsername(userRequestDto.username()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다. 확인 해주세요.");
        }
        // 이메일과 닉네임이 동일한지 확인
        if (fileUserRepository.findByEmail(userRequestDto.email()).isPresent()) {
            throw new IllegalArgumentException("닉네임은 존재 하는 이메일 입니다. 확인 해주새요. ");
        }
        // 상태 정보 시간에 따라 구분
        BinaryContent profileImage = binaryServiceImpl.createProfileImage(userProfileImgResponseDto.imgUrl());

        //dto를 엔티티로 변환
        User userMapper = UserMapper.toUserMapper(userRequestDto,profileImage.getId());

        UserStatus userStatus = new UserStatus(userMapper.getCreatedAt(), StatusType.Active.getExplanation());

        // 저장
        baseUserStatusRepository.save(userStatus);
        fileUserRepository.save(userMapper);

        // User -> UserNameResponse 변환
        return new UserNameStatusResponse(userMapper.getUsername(), userStatus.getStatusType());
    }


    @Override
    public UserResponse find(UUID userId) {
        User user = fileUserRepository.findById(userId.toString()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정 입니다"));
        UserStatus userStatus = baseUserStatusRepository.findUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("상태를 찾지 못 하였습니다."));
        return new UserResponse(user.getProfileId(), user.getUsername(), user.getEmail(), userStatus.getStatusType());
    }

    @Override
    public List<User> findAll() {
        List<User> userAll = fileUserRepository.findByAll();
        for(User user : userAll) {
            Optional<UserStatus> userId = baseUserStatusRepository.findUserId(user.getId());
            userId.orElseThrow(() -> new IllegalArgumentException("상태를 찾지 못 하였습니다."));
        }
        return fileUserRepository.findByAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = fileUserRepository.findById(userId.toString()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정 입니다"));
        user.update(newUsername, newEmail, newPassword);
        fileUserRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        fileUserRepository.delete(userId);
    }
}
