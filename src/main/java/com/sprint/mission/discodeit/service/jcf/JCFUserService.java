package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;


public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService() {
        this.data = new ArrayList<>();
    }


    // 사용자 생성
    @Override
    public void createUser(String name, String mail) {
        data.add(new User(name, mail));
        System.out.println("---------------------[사용자 등록]-------------------------");
        System.out.println("[완료]\n이름: " + name + "\n메일: " + mail);
        for (User u : data) {
            if (u.getName().equals(name) && u.getEmail().equals(mail)) {
                System.out.println("사용자 ID: " + u.getId());
                System.out.println("생성시간: " + u.getCreatedAtFormatted());
                System.out.println("업데이트 시간: " + u.getupdatedAttFormatted());
            }
        }
        System.out.println("---------------------------------------------------------");
    }


    // 사용자 조회
    @Override
    public User getUser(String name) {
        for (User userList : data) {  // foreach 가능?
            if (userList.getName().equals(name)) {
                return userList;
            }
        }
        return null;
    }

    public void foundUser(String name) {
        User userList = getUser(name);
        if (userList != null) {
            System.out.println("[사용자 조회]\n이름: " + userList.getName() + "\n메일: " + userList.getEmail());
            System.out.println("사용자 ID: " + userList.getId());
            System.out.println("생성 시간: " + userList.getCreatedAtFormatted());
            System.out.println("업데이트 시간: " + userList.getupdatedAttFormatted());
        } else {
            System.out.println("[사용자 조회]\n등록된 사용자가 존재하지 않습니다.");
        }
    }


    //사용자 전체 조회
    @Override
    public List<User> getAllUsers() {
        System.out.println("[사용자 전체 조회]");
        data.forEach(u -> {
            System.out.printf("이름: %-8s 메일: %s%n", u.getName(), u.getEmail());
        });
        return data;
    }


    // 사용자 수정
    @Override
    public void updateUser(String name, String changeName, String changeMail) {
        for (User userList  : data) {
            if (userList.getName().equals(name)) {
                userList.updateUser(changeName, changeMail);
                userList.update();
                System.out.printf("[사용자 정보 수정]\n[수정완료] 새로운 이름: %-8s 새로운 메일: %s%n", userList.getName(), userList.getEmail());
                System.out.println("업데이트 시간: " + getUser(changeName).getupdatedAttFormatted());
                return;
            }
        }
        System.out.println("[사용자 정보 수정]\n변경 할 사용자가 존재하지 않습니다.");

    }


    // 사용자 삭제
    public void deleteUser(String name) {
        for (User userList : data) {
            if (userList.getName().equals(name)) {
                data.remove(userList);
                System.out.printf("[사용자 정보 삭제]\n[삭제완료] 삭제 된 이름: %-10s 삭제 된 메일: %s%n", userList.getName(), userList.getEmail());
                return;
            }
        }
        System.out.println("[사용자 정보 삭제]\n삭제 할 사용자가 존재하지 않습니다.");
    }

}
