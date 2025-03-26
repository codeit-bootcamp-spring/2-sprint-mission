package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.userStatus.CreatedUserStatusParam;
import com.sprint.mission.discodeit.dto.service.userStatus.UpdateUserStatusParam;
import com.sprint.mission.discodeit.dto.service.userStatus.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatus create(UserStatus userStatus) {
        checkUserExists(userStatus);
        checkDuplicateUser(userStatus);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        UserStatus userStatus = findUserStatusById(id);
        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        return userStatuses;
    }

    @Override
    public UUID update(UpdateUserStatusParam updateUserStatusParam) {
        UserStatus userStatus = findUserStatusById(updateUserStatusParam.id());
        userStatus.updateUserStatus();
        userStatusRepository.save(userStatus);
        return userStatus.getId();
    }

    @Override
    public UUID updateByUserId(UUID userId, UpdateUserStatusParam updateUserStatusParam) {
        UserStatus userStatus = findUserStatusById(updateUserStatusParam.id());
        userStatus.updateUserStatus();
        return userStatus.getId();
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }

    private void checkUserExists(UserStatus userStatus) {
        userRepository.findById(userStatus.getUserId())
                .orElseThrow(() -> RestExceptions.USER_NOT_FOUND);
    }

    private void checkDuplicateUser(UserStatus userStatus) {
        if (userStatusRepository.existsByUserId(userStatus.getUserId())) {
            throw RestExceptions.DUPLICATE_USER_STATUS;
        }
    }


    private UserStatus findUserStatusById(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> RestExceptions.USER_STATUS_NOT_FOUND);
    }
}
