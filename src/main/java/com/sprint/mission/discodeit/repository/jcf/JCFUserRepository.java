package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.request.UpdateUserRequestDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Empty.EmptyUserListException;
import com.sprint.mission.discodeit.exception.NotFound.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.CommonUtils;
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
    public User findById(UUID userId) {
        return CommonUtils.findById(userList, userId, User::getId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));
    }

    @Override
    public List<User> findAll() {
        if (userList.isEmpty()) {
            throw new EmptyUserListException("유저 리스트가 비어있습니다.");
        }
        return userList;
    }

    @Override
    public User update(User user, UpdateUserRequestDTO updateUserRequestDTO, UUID newProfileId) {
        if (updateUserRequestDTO.replaceName() != null) {
            user.setName(updateUserRequestDTO.replaceName());
        }
        if (updateUserRequestDTO.replaceEmail() != null) {
            user.setEmail(updateUserRequestDTO.replaceEmail());
        }
        if (newProfileId != null) {
            user.setProfileId(newProfileId);
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

    @Override
    public boolean existId(UUID id) {
        return userList.stream().anyMatch(u -> u.getId().equals(id));
    }

    @Override
    public boolean existName(String name) {
        return userList.stream().anyMatch(u -> u.getName().equals(name));
    }

    @Override
    public boolean existEmail(String email) {
        return userList.stream().anyMatch(u -> u.getEmail().equals(email));
    }
}
