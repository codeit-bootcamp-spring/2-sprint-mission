package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UUID create(CreateUserStatusRequest request) {
        User user = userRepository.findByUserId(request.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("[ERROR] user not exist");
        }

        UserStatus checkUserStatus = userStatusRepository.findByUserId(request.getUserId());
        if (checkUserStatus != null) {
            throw new IllegalArgumentException("[ERROR] user status is already exist");
        }

        UserStatus userStatus = new UserStatus(request.getUserId());
        userStatusRepository.save(userStatus);

        return userStatus.getId();
    }

    @Override
    public UserStatus findByUserStatusId(UUID id) {
        return userStatusRepository.findByUserStatusId(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UUID update(UUID userStatusId, UpdateUserStatusRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserStatusId(userStatusId);

        userStatus.updateUserId(request.getUserId());
        userStatusRepository.save(userStatus);

        return userStatus.getId();
    }

    @Override
    public UUID updateByUserId(UUID userId, UpdateUserStatusRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);

        userStatus.updateUserId(request.getUserId());
        userStatusRepository.save(userStatus);

        return userStatus.getId();
    }

    @Override
    public void delete(UUID userId) {
        userStatusRepository.delete(userId);
    }
}
