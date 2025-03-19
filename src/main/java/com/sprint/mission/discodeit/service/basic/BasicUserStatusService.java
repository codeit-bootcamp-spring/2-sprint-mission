package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusFindResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateUserIdException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public void updateUserStatus(UserStatusUpdateRequest userStatusUpdateRequest) {
        this.userStatusRepository.updateTimeByUserId(userStatusUpdateRequest.readStatusId(), userStatusUpdateRequest.updateTime());
    }

    @Override
    public void updateUserStatusByUserId(UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest) {
        this.userStatusRepository.updateTimeByUserId(userStatusUpdateByUserIdRequest.userId(), userStatusUpdateByUserIdRequest.updateTime());
    }

    @Override
    public void deleteUserStatus(UUID id) {

    }
}
