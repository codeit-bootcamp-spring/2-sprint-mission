package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;


import java.util.*;
import java.util.stream.Collectors;

public class JCFUserRepositoryimplement implements UserRepository {
    Map<String, User> users = new HashMap<>(); //(userID/user)

    @Override
    public User readuser(String userId) {
        return users.get(userId);

    }

    @Override
    public boolean containsUser(String userId) {
        return users.containsKey(userId);
    }

    @Override
    public boolean registerUserId(User user) { //등록
        users.put(user.getuserId(), user);
       return containsUser(user.getuserId());

    }

    @Override
    public boolean removeUser(String userId) {//유저제거
        if (users.containsKey(userId)) {
            users.remove(userId);
           return users.containsKey(userId);
        }
        return false;
    }

    @Override
    public User updateUser(String userId) {
        // 유저가 존재하는 경우 업데이트
        User user = users.get(userId);
        user.setupdatedAt();

        return user;
    }

    @Override
    public List<String> allReadUsers() {
        return users.keySet().stream()
                .collect(Collectors.toList());
    }
}




