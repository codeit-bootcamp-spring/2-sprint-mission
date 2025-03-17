package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Exception.EmptyUserListException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
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
        User user = CommonUtils.findById(userList, userId, User::getId)
                .orElseThrow(() -> CommonExceptions.USER_NOT_FOUND);
        return user;
    }

    @Override
    public List<User> findUserList() {
        if (userList.isEmpty()) {
            throw new EmptyUserListException("유저 리스트가 비어있습니다.");
        }
        return userList;
    }


    @Override
    public UUID update(User user, UserCRUDDTO userUpdateDTO) {
        if (userUpdateDTO.userId() != null) {
            user.setId(userUpdateDTO.userId());
        }
        if (userUpdateDTO.userName() != null) {
            user.setName(userUpdateDTO.userName());
        }
        if (userUpdateDTO.email() != null) {
            user.setEmail(userUpdateDTO.email());
        }
        if (userUpdateDTO.profileId() != null) {
            user.setProfileId(userUpdateDTO.profileId());
        }
        return user.getId();
    }


    @Override
    public UUID remove(User user) {
        if (userList.isEmpty()) {
            throw new EmptyUserListException("유저 리스트가 비어있습니다.");
        }
        userList.remove(user);
        return user.getId();
    }
}
