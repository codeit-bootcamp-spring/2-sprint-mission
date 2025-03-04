package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService{
    private final Map<UUID, User> userList;

    public JCFUserService() {
        this.userList = new HashMap<>();
    }

    @Override
    public User creatUser(String userName, String userEmail, String userPassword){
        User user = new User(userName, userEmail, userPassword);
        this.userList.put(user.getId(), user);

        return user;
    }

    @Override
    public List<User> findAllUser(){
        return new ArrayList<>(userList.values());
    }

    @Override
    public User findByUserId(UUID userId){
        User userNullable = this.userList.get(userId);
        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
    }

    @Override
    public User updateUser(UUID userId, String newUsername, String newEmail, String newPassword){
        User userNullable = this.userList.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
        user.update(newUsername, newEmail, newPassword);

        return user;
    }


    @Override
    public void deleteUser(UUID userId){
        User removedUser = this.userList.remove(userId);
        if(removedUser == null) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다 : " + userId);
        }
    }
}