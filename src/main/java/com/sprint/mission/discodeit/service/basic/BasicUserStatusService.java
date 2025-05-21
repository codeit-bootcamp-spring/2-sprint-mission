package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusExistsException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    @Override
    public UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest) {

        User user = userRepository.findById(userStatusCreateRequest.userId())
                .orElseThrow(() -> new UserNotFoundException(userStatusCreateRequest.userId()));

        if (userStatusRepository.findByUserId(userStatusCreateRequest.userId()).isPresent()) {
            throw new UserStatusExistsException(userStatusCreateRequest.userId());
        }

        UserStatus newUserStatus = new UserStatus(user, userStatusCreateRequest.lastActiveAt());
        userStatusRepository.save(newUserStatus);

        return userStatusMapper.toDto(newUserStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public UserStatusDto findById(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> UserStatusNotFoundException.fromUserStatusId(id));

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(userStatusMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.fromUserStatusId(userStatusId));

        userStatus.update(userStatusUpdateRequest.newLastActiveAt());

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> UserStatusNotFoundException.fromUserId(userId));
        userStatus.update(userStatusUpdateByUserIdRequest.newLastActiveAt());

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
