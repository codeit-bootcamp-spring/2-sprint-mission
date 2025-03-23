package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User createUser(String userName, String email, String password) {
        User user = new User(userName, email, password);
        data.put(user.getId(), user);
        System.out.println("사용자가 생성되었습니다: \n" + user);
        return user;
    }

    @Override
    public User searchUser(UUID userId) {
        User user = findUser(userId);
        System.out.println("USER: " + user);
        return user;
    }

    @Override
    public List<User> searchAllUsers() {
        if (data.isEmpty()) {
            throw new NoSuchElementException("등록된 사용자가 존재하지 않습니다.");
        }
        List<User> users = new ArrayList<>(data.values());
        for (User user : users) {
            System.out.println("USER: " + user);
        }
        return users;

    }

    @Override
    public User updateAll(UUID userId, String userName, String email, String password) {
        User user = findUser(userId);
        user.updateAll(userName, email, password);
        System.out.println(userId + " 사용자 업데이트 완료되었습니다.");
        return user;
    }

    @Override
    public User updateUserName(UUID userId, String userName) {
        User user = findUser(userId);
        user.updateUserName(userName);
        System.out.println(userId + " 사용자 이름이 업데이트 완료되었습니다.");
        return user;
    }

    @Override
    public User updateEmail(UUID userId, String email) {
        User user = findUser(userId);
        user.updateEmail(email);
        System.out.println(userId + " 사용자 이메일이 업데이트 완료되었습니다.");
        return user;
    }

    @Override
    public User updatePassword(UUID userId, String password) {
        User user = findUser(userId);
        user.updatePassword(password);
        System.out.println(userId + " 사용자 비밀번호가 업데이트 완료되었습니다.");
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        findUser(userId);
        data.remove(userId);
        System.out.println(userId + " 사용자 삭제 완료되었습니다.");

    }

    public User findUser(UUID userId) {
        User user = data.get(userId);
        if (user == null) {
            throw new NoSuchElementException("해당 사용자가 존재하지 않습니다.");
        }
        return user;
    }
}
