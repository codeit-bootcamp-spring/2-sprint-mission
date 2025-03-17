package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.Exception.CommonException;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;

    void create(String userId) {
        try {
            UUID userUUID = UUID.fromString(userId);
            User user = userRepository.find(userUUID);
            UserStatus userStatus = userStatusRepository.find(userUUID);

            if (userStatus == null) {
                userStatus = new UserStatus(user.getId());
            } else {
                throw CommonExceptions.DUPLICATE_USER_STATUS;
            }
            userStatusRepository.save(userStatus);
        } catch (CommonException e) {
            System.out.println("에러 발생");
        }
    }

    UserStatus find(String userId) {
        UUID userUUID = UUID.fromString(userId);
        UserStatus userStatus = userStatusRepository.find(userUUID);
        return userStatus;
    }

    List<UserStatus> findAll() {
        List<UserStatus> all = userStatusRepository.findAll();
        return all;
    }

    void update(String userId, String replaceId) {
        UUID userUUID = UUID.fromString(userId);
        UUID replaceUUID = UUID.fromString(replaceId);

        UserStatus userStatus = userStatusRepository.find(userUUID);
        UserStatusUpdateDTO userStatusUpdateDTO = new UserStatusUpdateDTO(replaceUUID);

        userStatusRepository.update(userStatus, userStatusUpdateDTO);
    }

    void delete(String userId) {
        UUID userUUID = UUID.fromString(userId);
        userStatusRepository.delete(userUUID);
    }
}
