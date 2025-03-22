package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.Valid.DuplicateUserStatusException;
import com.sprint.mission.discodeit.logging.CustomLogging;
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
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;

    @Override
    @CustomLogging
    public UserStatus create(UUID userId) {

        User user = userRepository.findById(userId);
        UserStatus userStatus = userStatusRepository.findByUserId(userId);

        if (userStatus == null) {
            userStatus = new UserStatus(user.getId());
        } else {
            throw new DuplicateUserStatusException("중복된 유저 상태가 있습니다.");
        }
        UserStatus save = userStatusRepository.save(userStatus);
        return save;
    }


    @Override
    public UserStatus findByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        return userStatus;
    }

    @Override
    public UserStatus findByStatusId(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findByStatusId(userStatusId);
        return userStatus;
    }


    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> all = userStatusRepository.findAll();
        return all;
    }

    @Override
    @CustomLogging
    public UUID update(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);

        UserStatus update = userStatusRepository.update(userStatus);
        return update.getUserStatusId();
    }

    @Override
    @CustomLogging
    public void delete(UUID userStatusId) {
        userStatusRepository.delete(userStatusId);
    }
}
