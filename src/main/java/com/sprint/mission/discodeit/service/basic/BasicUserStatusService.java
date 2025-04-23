package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.LogicException;
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
    public UserStatusDto create(UserStatusCreateDto userStatusCreateDto) {

        // userService에서 create하면서 이 메서드를 호출하는데 굳이 다시 findById로 user를 찾아야할까,,? user를 넘겨줘도 되지 않을까,,? 지금이 더 안전하긴한데,,
        User user = userRepository.findById(userStatusCreateDto.userId())
                .orElseThrow(() -> new LogicException(ErrorCode.USER_NOT_FOUND));

        if (userStatusRepository.findByUserId(userStatusCreateDto.userId()).isPresent()) {
            throw new LogicException(ErrorCode.USER_STATUS_ALREADY_EXISTS);
        }

        UserStatus newUserStatus = new UserStatus(user, userStatusCreateDto.lastActiveAt());
        userStatusRepository.save(newUserStatus);

        return userStatusMapper.toDto(newUserStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public UserStatusDto findById(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new LogicException(ErrorCode.USER_STATUS_NOT_FOUND));

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
    public UserStatusDto update(UUID userStatusId, UserStatusUpdateDto userStatusUpdateDto) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new LogicException(ErrorCode.USER_STATUS_NOT_FOUND));

        userStatus.update(userStatusUpdateDto.newLastActiveAt());

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateByUserIdDto userStatusUpdateByUserIdDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new LogicException(ErrorCode.USER_STATUS_NOT_FOUND));
        userStatus.update(userStatusUpdateByUserIdDto.newLastActiveAt());

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
