package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.RequestToService.UserUpdateDTO;
import com.sprint.mission.discodeit.DTO.legacy.User.UserCRUDDTO;
import com.sprint.mission.discodeit.Exception.Empty.EmptyUserListException;
import com.sprint.mission.discodeit.Exception.NotFound.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserRepository implements UserRepository {
    private List<User> userList = new ArrayList<>();

    @Override
    public void reset() {
        userList = new ArrayList<>();
    }

    @Override
    public User save(User user) {
        userList.add(user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        User user = CommonUtils.findById(userList, userId, User::getId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));
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
    public User update(User user, UserUpdateDTO userUpdateDTO,UUID replaceProfileId) {
        if (userUpdateDTO.replaceName() != null) {
            user.setName(userUpdateDTO.replaceName());
        }
        if (userUpdateDTO.replaceEmail() != null) {
            user.setEmail(userUpdateDTO.replaceEmail());
        }
        if (replaceProfileId != null) {
            user.setProfileId(replaceProfileId);
        }
        return user;
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
