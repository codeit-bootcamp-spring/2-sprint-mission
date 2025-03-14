package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class JCFUserService implements UserService {
    private final List<User> usersData;

    public JCFUserService() {
        usersData = new ArrayList<>();
    }


    // 사용자 생성
    @Override
    public User create(String name, String email, String password) {
        User user = new User(name, email, password);
        if (!validateUser(user)) {
            return null;
        }
        return createUser(user);
    }

    private User createUser(User user) {
        usersData.add(user);
        System.out.println(user);
        return user;
    }

    private boolean validateUser(User user) {
        if (getUser(user.getId()) != null) {
            System.out.println("등록된 사용자가 존재합니다.");
            return false;
        }
        return true;
    }


    // 사용자 조회
    @Override
    public User getUser(UUID userId) {
        return find(userId);
    }

    private User find(UUID userId) {
        for (User userList : usersData) {
            if (userList.getId().equals(userId)) {
                return userList;
            }
        }
        return null;
    }


    //사용자 전체 조회
    @Override
    public List<User> getAllUser() {
        return findAllUser();
    }

    private List<User> findAllUser() {
        if (usersData.isEmpty()) {
            System.out.println("전체 조회 결과가 없습니다.");
            return Collections.emptyList();
        }
        for (User userList : usersData) {
        }
        return usersData;
    }


    // 사용자 수정
    @Override
    public User update(UUID userId, String changeName, String changeEmail, String changePassword) {
        return updateUser(userId, changeName, changeEmail, changePassword);
    }

    private User updateUser(UUID userId, String changeName, String changeEmail, String changePassword) {
        for (User userList : usersData) {
            if (userList.getId().equals(userId)) {
                userList.update(changeName, changeEmail, changePassword);
                System.out.printf("[ %s ], [ %s ] 로 변경되었습니다.", userList.getName(), userList.getEmail());
                return userList;
            }
        }
        System.out.println("변경 할 사용자가 존재하지 않습니다.");
        return null;
    }


    // 사용자 삭제
    @Override
    public void delete(UUID userId) {
        for (User userList : usersData) {
            if (userList.getId().equals(userId)) {
                usersData.remove(userList);
                System.out.println("[ " + userList.getName() + " ] 이 삭제 되었습니다.");
            }
        }
        System.out.println("삭제 할 사용자가 존재하지 않습니다.");
    }
}
