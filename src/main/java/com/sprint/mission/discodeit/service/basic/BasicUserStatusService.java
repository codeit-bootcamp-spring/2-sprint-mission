package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.LogicException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
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
            throw new LogicException(ErrorCode.USER_NOT_FOUND);
        }

        boolean isExistUserStatus = userStatusRepository.findByUserId(userStatusCreateDto.userId()) != null;
        if (isExistUserStatus) {
            throw new LogicException(ErrorCode.USER_STATUS_ALREADY_EXISTS);
        }

        UserStatus newUserStatus = new UserStatus(userStatusCreateDto.userId(), userStatusCreateDto.lastActiveAt());
        return userStatusRepository.save(newUserStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        UserStatus userStatus = userStatusRepository.findByUserId(id);

        if (userStatus == null) {
            throw new LogicException(ErrorCode.USER_STATUS_NOT_FOUND);
        }

        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusUpdateDto userStatusUpdateDto) {
        UserStatus userStatus = findById(userStatusId);
        userStatus.update(userStatusUpdateDto.newLastActiveAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateByUserIdDto userStatusUpdateByUserIdDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);

        if (userStatus == null) {
            throw new LogicException(ErrorCode.USER_STATUS_NOT_FOUND);
        }
        userStatus.update(userStatusUpdateByUserIdDto.newLastActiveAt());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.delete(id);
    }
}
