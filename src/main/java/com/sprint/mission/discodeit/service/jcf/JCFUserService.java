package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID,User> userData;

    public JCFUserService() {
        this.userData = new HashMap<>();
    }

    @Override
    public void create(String userName) {
        if(read(userName)!=null){
            System.out.println("이미 존재하는 유저입니다.");
            return;
        }
        User newUser = new User(userName);
        userData.put(newUser.getId(), newUser);
    }

    @Override
    public void delete(String userName) {
        User deletedUser = read(userName);
        if(deletedUser==null){
            System.out.println("존재하는 유저가 없습니다.");
            return;
        }
        userData.remove(deletedUser.getId());
    }

    @Override
    public void update(String oldName,String newName) {
        User user = read(oldName);
        user.updateUser(newName);
    }

    @Override
    public User read(String userName) {
        return userData.values().stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Map<UUID,User> readAll() {
        return userData;
    }

    @Override
    public String toString() {
        return "JCFUserService{" +
                "userData=" + userData +
                '}';
    }
}
