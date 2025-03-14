package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.UserStatusCreateDTO;
import com.sprint.mission.discodeit.DTO.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.Exception.DuplicateUserStatusException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
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
            UUID UID = UUID.fromString(userId);
            User user = userRepository.findUserByUserId(UID);
            UserStatus userStatus = userStatusRepository.find(UID);

            if (userStatus == null) {
                userStatus = new UserStatus(user.getId());
            } else {
                throw new DuplicateUserStatusException("해당 유저 상태는 이미 존재합니다.");
            }
            UserStatusCreateDTO userStatusCreateDTO = new UserStatusCreateDTO(user.getId(), userStatus);
            userStatusRepository.save(userStatusCreateDTO);
        } catch (UserNotFoundException e) {
            System.out.println("해당 유저는 존재하지 않습니다.");
        } catch (DuplicateUserStatusException e) {
            System.out.println("해당 유저 상태는 이미 존재합니다.");
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
        UserStatusUpdateDTO userStatusUpdateDTO = new UserStatusUpdateDTO(userUUID, replaceUUID);
        userStatusRepository.update(userStatusUpdateDTO);
    }

    void delete(String userId) {
        UUID userUUID = UUID.fromString(userId);
        userStatusRepository.delete(userUUID);
    }
}
