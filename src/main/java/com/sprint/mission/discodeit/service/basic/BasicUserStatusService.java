package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.*;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.common.NoSuchIdException;
import com.sprint.mission.discodeit.exception.user.DuplicateUserIdException;
import com.sprint.mission.discodeit.exception.userstatus.UpdateUserStatusException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UUID createUserStatus(UserStatusCreateRequest userStatusCreateRequest) {
        UserService.validateUserId(userStatusCreateRequest.userId(), userRepository);
        if (!this.userStatusRepository.existsByUserId(userStatusCreateRequest.userId())) {
            throw new DuplicateUserIdException(userStatusCreateRequest.userId());
        }
        UserStatus newUserStatus = new UserStatus(userStatusCreateRequest.userId());
        this.userStatusRepository.add(newUserStatus);
        return newUserStatus.getId();
    }

    @Override
    public UserStatusFindResponse findUserStatus(UUID id) {
        UserStatus findUserStatus = this.userStatusRepository.findById(id);
        return new UserStatusFindResponse(findUserStatus.getId(), findUserStatus.getUserId());
    }

    @Override
    public List<UserStatusFindResponse> findAllUserStatus() {
        return this.userStatusRepository.getAll().values().stream()
                .map(userStatus -> new UserStatusFindResponse(userStatus.getId(), userStatus.getUserId()))
                .toList();
    }

    @Override
    public void updateTimeById(UserStatusTimeUpdateRequest userStatusUpdateRequest) {
        this.userStatusRepository.updateTimeById(userStatusUpdateRequest.userStatusId(), userStatusUpdateRequest.updateTime());
    }

    @Override
    public void updateTimeByUserId(UserStatusTimeUpdateByUserIdRequest userStatusTimeUpdateByUserIdRequest) {
        this.userStatusRepository.updateTimeByUserId(userStatusTimeUpdateByUserIdRequest.userId(), userStatusTimeUpdateByUserIdRequest.updateTime());
    }

    @Override
    public void updateUserStatusByUserId(UUID id, UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest) {
        try {
            this.userStatusRepository.updateUserStatusByUserId(id, userStatusUpdateByUserIdRequest.type());
        } catch (NoSuchIdException e) {
            throw new UpdateUserStatusException(e.getMessage(), e.getStatus(), e);
        } catch (Exception e) {
            throw new UpdateUserStatusException("userStatus 업데이트 중 예상치 못한 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public void deleteUserStatus(UUID id) {
        this.userStatusRepository.deleteById(id);
    }
}
