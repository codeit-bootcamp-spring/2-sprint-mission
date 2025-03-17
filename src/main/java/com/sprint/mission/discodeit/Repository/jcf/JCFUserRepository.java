package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                .orElseThrow(() -> CommonExceptions.USER_NOT_FOUND);
        return user;
    }

    @Override
    public List<User> findUserList() {
        if (userList.isEmpty()) {
            throw CommonExceptions.EMPTY_USER_LIST;
        }
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
        if (userList.isEmpty()) {
            throw CommonExceptions.EMPTY_USER_LIST;
        }
        userList.remove(user);
        return user.getId();
    }
}
