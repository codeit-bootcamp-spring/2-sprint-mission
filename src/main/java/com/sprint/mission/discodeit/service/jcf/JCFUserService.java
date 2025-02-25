package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;


public class JCFUserService implements UserService {
    private final List<User> usersData;

    public JCFUserService() {
        usersData = new ArrayList<>();
    }

    public List<User> getUsersData() {
        return usersData;
    }

    // 사용자 생성
    @Override
    public void createUser(User user) {
        if (!usersData.contains(user)) {
            usersData.add(user);
            System.out.println("-------------------[사용자 등록 결과]-----------------------");
            System.out.println("이름: " + user.getName() + "\n메일: " + user.getEmail());
            System.out.println("사용자 ID: " + user.getId());
            System.out.println("생성 시간: " + user.getCreatedAtFormatted());
            System.out.println("생성 시간: " + user.getupdatedAttFormatted());
            System.out.println("---------------------------------------------------------");
        } else {
            System.out.println("---------------------------------------------------------");
            System.out.println("등록된 사용자가 존재합니다.");
            System.out.println("---------------------------------------------------------");
        }

    }


    // 사용자 조회
    @Override
    public void getUser(String name) {
        boolean found = false;
        for (User userList : usersData) {
            if (userList.getName().equals(name)) {
                System.out.println("--------------------[사용자 조회 결과]---------------------");
                System.out.println("[사용자 조회]\n이름: " + userList.getName() + "\n메일: " + userList.getEmail());
                System.out.println("사용자 ID: " + userList.getId());
                System.out.println("생성 시간: " + userList.getCreatedAtFormatted());
                System.out.println("업데이트 시간: " + userList.getupdatedAttFormatted());
                System.out.println("---------------------------------------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("---------------------------------------------------------");
            System.out.println("등록된 사용자가 존재하지 않습니다.");
            System.out.println("---------------------------------------------------------");
        }
    }

    //사용자 전체 조회
    @Override
    public void getAllUsers() {
        System.out.println("------------------[사용자 전체 조회 결과]-------------------");
        for (User userList : usersData) {
            System.out.printf("이름: %-8s 메일: %s\n생성 시간: %s\n업데이트 시간: %s\n\n",
                    userList.getName(), userList.getEmail(),
                    userList.getCreatedAtFormatted(), userList.getupdatedAttFormatted());
            System.out.println("---------------------------------------------------------");
        }
    }

    // 사용자 수정
    @Override
    public void updateUser(String name, String changeName, String changeMail) {
        String oldName;
        for (User userList : usersData) {
            if (userList.getName().equals(name)) {
                oldName = userList.getName();
                userList.updateUser(changeName, changeMail);
                System.out.println("------------------[사용자 정보 수정 결과]-------------------");
                System.out.printf("기존 이름: %-8s새로운 이름: %-8s 새로운 메일: %s%n", oldName, userList.getName(), userList.getEmail());
                System.out.println("업데이트 시간: " + userList.getupdatedAttFormatted());
                System.out.println("---------------------------------------------------------");
                return;
            }
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("변경 할 사용자가 존재하지 않습니다.");
        System.out.println("---------------------------------------------------------");

    }


    // 사용자 삭제
    @Override
    public void deleteUser(String name) {
        for (User userList : usersData) {
            if (userList.getName().equals(name)) {
                usersData.remove(userList);
                System.out.println("--------------------[사용자 삭제 결과]---------------------");
                System.out.printf("삭제 된 이름: %-8s 삭제 된 메일: %s%n", userList.getName(), userList.getEmail());
                System.out.println("---------------------------------------------------------");
                return;
            }
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("삭제 할 사용자가 존재하지 않습니다.");
        System.out.println("---------------------------------------------------------");
    }

}
