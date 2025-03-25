package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.DeleteUserRequestDto;
import com.sprint.mission.discodeit.dto.FindUserDto;
import com.sprint.mission.discodeit.dto.SaveUserParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserParamDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void save(SaveUserParamDto saveUserParamDto) {
        if (userRepository.findUserByUsername(saveUserParamDto.username()).isPresent()) {
            throw new IllegalArgumentException("사용자 이름 중복");
        }

        if (userRepository.findUserByEmail(saveUserParamDto.email()).isPresent()) {
            throw new IllegalArgumentException("이메일 중복");
        }

        User user = new User(
                saveUserParamDto.username(), saveUserParamDto.password(),
                saveUserParamDto.nickname(), saveUserParamDto.email(),
                saveUserParamDto.profileUUID());
        userRepository.save(user);
        UserStatus userStatus = UserStatus.builder()
                .userUUID(user.getId())
                .build();
        userStatusRepository.save(userStatus);
    }

    @Override
    public FindUserDto findByUser(UUID userUUID) {
        User user = userRepository.findUserById(userUUID)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 상태 확인 불가"));

        FindUserDto findUserDto = new FindUserDto(
                user.getId(), user.getNickname(),
                user.getProfile(), user.getCreatedAt(),
                user.getUpdatedAt(), userStatus.getUpdatedAt(),
                userStatus.isLastStatus());

        return findUserDto;
    }

    @Override
    public List<FindUserDto> findAllUser() {
        List<User> userList = userRepository.findAllUser();

        return userList.stream()
                .map(user -> findByUser(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(UpdateUserParamDto updateUserDto) {
        User user = userRepository.findUserById(updateUserDto.userUUID()).
                orElseThrow(() -> new NullPointerException("사용자가 존재하지 않습니다."));
        userRepository.update(updateUserDto.userUUID(), updateUserDto.nickname(), updateUserDto.profileId());
    }

    @Override
    public void delete(DeleteUserRequestDto deleteUserRequestDto) {
        User user = userRepository.findUserById(deleteUserRequestDto.id())
                .orElseThrow(() -> new NullPointerException("사용자가 존재하지 않습니다."));

        if(user.getProfile() != null) {
            binaryContentRepository.delete(user.getProfile());
        }

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 상태 오류"));
        userStatusRepository.delete(userStatus.getId());
        userRepository.deleteUserById(user.getId());
    }
}
