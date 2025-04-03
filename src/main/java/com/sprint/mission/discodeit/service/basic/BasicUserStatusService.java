package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserStatus create(UserStatus userStatus) {
        checkUserExists(userStatus);
        checkDuplicateUser(userStatus);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        return findUserStatusById(id);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return findUserStatusByUserId(userId);
    }

    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        return userStatuses;
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus userStatus = findUserStatusByUserId(userId);
        userStatus.updateUserStatus();
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatusRepository.deleteByUserId(userId);
    }

    private void checkUserExists(UserStatus userStatus) {
        userRepository.findById(userStatus.getUserId())
                .orElseThrow(() -> {
                    logger.error("유저상태 생성 중 유저 찾기 실패: {}", userStatus.getUserId());
                    return RestExceptions.USER_NOT_FOUND;
                });
    }

    private void checkDuplicateUser(UserStatus userStatus) {
        if (userStatusRepository.existsByUserId(userStatus.getUserId())) {
            logger.error("유저상태 생성 중 유저상태 중복 발생: {}", userStatus.getId());
            throw RestExceptions.DUPLICATE_USER_STATUS;
        }
    }


    private UserStatus findUserStatusByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    logger.error("유저상태 조회 실패 - userId: {}", userId);
                    return RestExceptions.USER_STATUS_NOT_FOUND;
                });
    }

    private UserStatus findUserStatusById(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("유저상태 조회 실패: {}", id);
                    return RestExceptions.USER_STATUS_NOT_FOUND;
                });
    }
}
