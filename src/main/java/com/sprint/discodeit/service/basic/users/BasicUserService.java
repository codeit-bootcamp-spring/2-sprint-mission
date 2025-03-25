package com.sprint.discodeit.service.basic.users;

import com.sprint.discodeit.domain.dto.userDto.UserNameStatusResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserProfileImgResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserResponseDto;
import com.sprint.discodeit.domain.dto.userDto.UserUpdateRequestDto;
import com.sprint.discodeit.domain.entity.BinaryContent;
import com.sprint.discodeit.domain.StatusType;
import com.sprint.discodeit.domain.entity.UserStatus;
import com.sprint.discodeit.domain.mapper.UserMapper;
import com.sprint.discodeit.domain.dto.userDto.UserRequestDto;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.repository.file.BaseBinaryContentRepository;
import com.sprint.discodeit.repository.file.BaseUserStatusRepository;
import com.sprint.discodeit.repository.file.FileUserRepository;
import com.sprint.discodeit.service.UserServiceV1;
import com.sprint.discodeit.service.basic.util.BinaryGenerator;
import com.sprint.discodeit.service.basic.util.UserStatusEvaluator;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserServiceV1 {

    private final FileUserRepository fileUserRepository;
    private final BinaryGenerator binaryGenerator;
    private final UserStatusEvaluator userStatusEvaluator;
    private final BaseUserStatusRepository baseUserStatusRepository;
    private final BaseBinaryContentRepository baseBinaryContentRepository;

    public UserNameStatusResponseDto create(UserRequestDto userRequestDto, UserProfileImgResponseDto userProfileImgResponseDto) {
        if (userRequestDto == null) {
            throw new IllegalArgumentException("사용자 요청 정보가 없습니다.");
        }
        if (userProfileImgResponseDto == null) {
            throw new IllegalArgumentException("사용자 프로필 이미지 정보가 없습니다.");
        }

        // 중복된 유저 확인
        if (fileUserRepository.findByUsername(userRequestDto.username()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다. 확인 해주세요.");
        }
        // 이메일과 닉네임이 동일한지 확인
        if (fileUserRepository.findByEmail(userRequestDto.email()).isPresent()) {
            throw new IllegalArgumentException("닉네임은 존재 하는 이메일 입니다. 확인 해주새요. ");
        }
        // 이미지 기본 또는 사용자 지정 이미지 처리 하는 서비스
        BinaryContent profileImage = binaryGenerator.createProfileImage(userProfileImgResponseDto.imgUrl());

        //dto를 엔티티로 변환
        User userMapper = UserMapper.toUserMapper(userRequestDto);

        UserStatus userStatus = new UserStatus(userMapper.getCreatedAt(), StatusType.Active.getExplanation());

        //연관관계 메소드로 상태 정보 주입
        userMapper.associateStatus(userStatus);
        userMapper.associateProfileId(profileImage);

        // 저장
        baseUserStatusRepository.save(userStatus);
        fileUserRepository.save(userMapper);
        baseBinaryContentRepository.save(profileImage);

        // User -> UserNameResponse 변환
        return new UserNameStatusResponseDto(userMapper.getUsername(), userStatus.getStatusType(), userMapper.getId());
    }


    @Override
    public UserResponseDto find(UUID userId) {
        User user = fileUserRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정 입니다"));
        UserStatus userStatus = baseUserStatusRepository.findById(user.getUserStatusId())
                        .orElseThrow(() -> new IllegalArgumentException("사용자에 상태를 찾을 수 없습니다. "));
        String status = userStatusEvaluator.determineUserStatus(userStatus.getLastLoginTime());
        return new UserResponseDto(user.getProfileId(), user.getUsername(), user.getEmail(), status);
    }

    @Override
    public List<User> findAll() {
        List<User> userAll = fileUserRepository.findByAll();
        Map<UUID, UserStatus> byAllAndUser = baseUserStatusRepository.findByAllAndUser(userAll);
        System.out.println(byAllAndUser);
        return userAll;
//        userAll.stream().map(user -> {
//            UserStatus userStatus = byAllAndUser.get(user.getUserStatusId().toString());
//
//            //  상태 판별 수행
//            String status = userStatusEvaluator.determineUserStatus(userStatus.getLastLoginTime())
//                    : StatusType.Inactive.getExplanation();
//        })
//        return fileUserRepository.findByAll();
    }

    @Override
    public UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto) {
        //조회
        User user = fileUserRepository.findById(userUpdateRequestDto.userId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정 입니다"));

        //가공
        BinaryContent profileImage = binaryGenerator.createProfileImage(userUpdateRequestDto.profileImg());

        //저장
        user.associateProfileId(profileImage);
        baseBinaryContentRepository.save(profileImage);
        user.update(userUpdateRequestDto.newUsername(), userUpdateRequestDto.newEmail(), userUpdateRequestDto.newPassword());
        fileUserRepository.save(user);

        return new UserResponseDto(user.getProfileId(), userUpdateRequestDto.newUsername(), userUpdateRequestDto.newEmail(), StatusType.Active.toString());
    }

    @Override
    public void delete(UUID userId) {
        Optional<User> user = fileUserRepository.findById(userId);
        if (user.isPresent()) {
            user.get().isDeleted();
            UserStatus userStatus = new UserStatus(Instant.now(), StatusType.Inactive.getExplanation());
            baseUserStatusRepository.save(userStatus);
            fileUserRepository.save(user.get());
        }else{
            throw new IllegalArgumentException("삭제된 회원 입니다");
        }
    }
}
