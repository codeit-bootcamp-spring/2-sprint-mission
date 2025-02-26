package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class JCFUserService implements UserService {
    public final List<User> usersData;

    public JCFUserService() {
        usersData = new ArrayList<>();
    }


    // 사용자 생성
    @Override
    public User create(User user) {
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
        if (getUser(user.getName()) != null) {
            System.out.println("등록된 사용자가 존재합니다.");
            return false;
        }
        return true;
    }


    // 사용자 조회
    @Override
    public User getUser(String name) {
        return find(name);
    }

    private User find(String name) {
        for (User userList : usersData) {
            if (userList.getName().equals(name)) {
                System.out.println(userList);
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
            System.out.println(userList);
        }
        return usersData;
    }


    // 사용자 수정
    @Override
    public User update(String name, String changeName, String changeEmail) {
        return updateUser(name, changeName, changeEmail);
    }

    private User updateUser(String name, String changeName, String changeEmail) {
        for (User userList : usersData) {
            if (userList.getName().equals(name)) {
                userList.update(changeName, changeEmail);
                System.out.printf("[ %s ], [ %s ] 로 변경되었습니다.", userList.getName(), userList.getEmail());
                return userList;
            }
        }
        System.out.println("변경 할 사용자가 존재하지 않습니다.");
        return null;
    }


    // 사용자 삭제
    @Override
    public User delete(String name) {
        return deleteUser(name);
    }

    private User deleteUser(String name) {
        for (User userList : usersData) {
            if (userList.getName().equals(name)) {
                usersData.remove(userList);
                System.out.println("[ " + userList.getName() + " ] 이 삭제 되었습니다.");
                return userList;
            }
        }
        System.out.println("삭제 할 사용자가 존재하지 않습니다.");
        return null;
    }
}
