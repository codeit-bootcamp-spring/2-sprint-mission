package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {
    private List<User> userList = new ArrayList<>();

    @Override
    public void reset() {
        userList = new ArrayList<>();
    }

    @Override
    public UUID save(User user) {

        userList.add(user);

        return user.getId();
    }

    @Override
    public User find(UUID userId) {
        User user = userList.stream().filter(u -> u.getId().equals(userId)).findFirst()
                .orElseThrow(() -> new UserNotFoundException("해당 ID를 가지는 유저를 찾을 수 없습니다."));
        return user;
    }

    @Override
    public List<User> findUserList() {
        return userList;
    }


    @Override
    public UUID update(User user, UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.replaceId() != null) {
            user.setId(userUpdateDTO.replaceId());
        }
        if (userUpdateDTO.replaceName() != null) {
            user.setName(userUpdateDTO.replaceName());
        }
        if (userUpdateDTO.replaceEmail() != null) {
            user.setEmail(userUpdateDTO.replaceEmail());
        }
        if (userUpdateDTO.binaryContentId() != null) {
            user.setProfileId(userUpdateDTO.binaryContentId());
        }
        return user.getId();
    }


    @Override
    public UUID remove(User user) {
        userList.remove(user);
        return user.getId();
    }
}
