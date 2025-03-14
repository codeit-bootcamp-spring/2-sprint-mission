package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

            UserStatus userStatus = new UserStatus(user.getId());
            userStatusRepository.save(userStatus);
        } catch (UserNotFoundException e) {
            System.out.println("해당 유저는 존재하지 않습니다.");
        }
    }
}
