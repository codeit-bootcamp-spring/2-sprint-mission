package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

// UserService interface를 참조하여 기능을 구현한다.
public class JCFUserService implements UserService {
    // user 목록
    private final Map<UUID, User> usermap = new HashMap<>();

    // Create - 생성
    @Override
    public void createUser(String name) {
        // 생성자를 통해 id 생성
        User user = new User(name);
        usermap.put(user.getId(), user);
    }

    // Read - 읽기, 조회
    @Override
    public List<User> getAllUser(){
        List<User> userList = new ArrayList<>(usermap.values());
        return Optional.ofNullable(userList).orElse(Collections.emptyList());
    }
    @Override
    public User getOneUser(UUID id){
        User user = usermap.get(id);
        if (user == null) {
            throw new NoSuchElementException("User not found with id: " + id);
        }
        return user;
    }
    // Update - 수정

    @Override
    public void updateUser(String newName, UUID id) {
        usermap.get(id).updateUser(newName);
    }

    @Override
    public void deleteUser(UUID id) {
        usermap.remove(id);
    }
}
