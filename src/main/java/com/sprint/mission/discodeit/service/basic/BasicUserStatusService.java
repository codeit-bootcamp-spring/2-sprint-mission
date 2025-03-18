package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateDto userStatusCreateDto) {
        boolean isExitUser = userRepository.findById(userStatusCreateDto.userId()) != null;
        if (!isExitUser) {
            throw new NoSuchElementException(userStatusCreateDto.userId() + " 유저를 찾을 수 없습니다.");
        }

        boolean isExistUserStatus = userStatusRepository.findByUserId(userStatusCreateDto.userId()) != null;
        if (isExistUserStatus) {
            throw new RuntimeException(userStatusCreateDto.userId() + " 유저의 상태가 이미 존재합니다.");
        }

        UserStatus newUserStatus = new UserStatus(userStatusCreateDto.userId(), userStatusCreateDto.lastActiveAt());
        return userStatusRepository.save(newUserStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        UserStatus userStatus = userStatusRepository.findByUserId(id);

        if (userStatus == null) {
            throw new NoSuchElementException(id + " 유저 상태를 찾을 수 없습니다.");
        }

        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateDto userStatusUpdateDto) {
        UserStatus userStatus = findById(userStatusUpdateDto.id());
        userStatus.update(userStatusUpdateDto.newLastActiveAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UserStatusUpdateByUserIdDto userStatusUpdateByUserIdDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(userStatusUpdateByUserIdDto.userId());
        userStatus.update(userStatusUpdateByUserIdDto.newLastActiveAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.delete(id);
    }
}
