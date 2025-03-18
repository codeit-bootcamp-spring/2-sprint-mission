package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.UserStatus.UserStatusCRUDDTO;
import com.sprint.mission.discodeit.Exception.Valid.DuplicateUserStatusException;
import com.sprint.mission.discodeit.Exception.legacy.NotFoundException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.logging.CustomLogging;
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
    public void create(String userId) {
        try {
            UUID userUUID = UUID.fromString(userId);
            User user = userRepository.find(userUUID);
            UserStatus userStatus = userStatusRepository.find(userUUID);

            if (userStatus == null) {
                userStatus = new UserStatus(user.getId());
            } else {
                throw new DuplicateUserStatusException("중복된 유저 상태가 있습니다.");
            }
            userStatusRepository.save(userStatus);
        } catch (NotFoundException e) {
            System.out.println("에러 발생");
        }
    }


    @Override
    public UserStatus find(String userId) {
        UUID userUUID = UUID.fromString(userId);
        UserStatus userStatus = userStatusRepository.find(userUUID);
        return userStatus;
    }


    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> all = userStatusRepository.findAll();
        return all;
    }

    @Override
    @CustomLogging
    public void update(String userId, String replaceId) {
        UUID userUUID = UUID.fromString(userId);
        UUID replaceUUID = UUID.fromString(replaceId);

        UserStatus userStatus = userStatusRepository.find(userUUID);
        UserStatusCRUDDTO userStatusUpdateDTO = UserStatusCRUDDTO.update(replaceUUID);

        userStatusRepository.update(userStatus, userStatusUpdateDTO);
    }

    @Override
    @CustomLogging
    public void delete(String userId) {
        UUID userUUID = UUID.fromString(userId);
        userStatusRepository.delete(userUUID);
    }
}
