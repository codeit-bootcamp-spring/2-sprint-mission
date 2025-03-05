package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userData; // 타입 선언, 고유 UUID가 키
    // UUID : [id, createdAt, updatedAt, userName]
    //Map<>으로 타입 선언 -> 다형성, 유연성 up
    private static JCFUserService INSTANCE = new JCFUserService();

    private JCFUserService() {
        this.userData = new HashMap<>(); //구현체 생성, 초기화
    }

    public static JCFUserService getInstance() {
        // instance 가 null 일 때만 생성
        if (INSTANCE == null) {
            INSTANCE = new JCFUserService();
        }
        return INSTANCE;
    }

    @Override
    public User createUser(String userName) {
        User user = new User(userName); //유저 객체 생성
        userData.put(user.getId(), user); // Map에 저장 (힙 메모리)
        System.out.println("사용자 생성 : " + userData.get(user.getId()));
        return user;
    }

    @Override
    public void getUserInfo(String userName) {
        User user = findUserEntry(userName);
        if (user == null) {
            System.out.println("조회한 사용자 정보가 없습니다.");
            return;
        }
        System.out.println("사용자 정보 조회: " + user);
    }

    @Override
    public void getAllUserData() {
        if (userData.isEmpty()) {
            System.out.println("사용자 데이터가 없습니다.");
            return;
        }
        System.out.println("모든 사용자 데이터 : " + userData);
    }

    @Override
    public void updateUserName(String oldUserName, String newUserName) {
        User user = findUserEntry(oldUserName);
        if (user == null) {
            System.out.println("업데이트할 사용자가 존재하지 않습니다.");
            return;
        }
        user.setUserName(newUserName);
        System.out.println("사용자 정보가 업데이트 됐습니다." + "-> " + findUserEntry(newUserName));
    }

    @Override
    public void deleteUserName(String userName) {
        User user = findUserEntry(userName);
        if (user == null) {
            System.out.println("삭제할 사용자가 존재하지 않습니다.");
            return;
        }
        userData.remove(user.getId());
        System.out.println("사용자가 삭제됐습니다 : " + user);
    }

    public UUID findUserId(String userName) {
        User user = userData.values().stream()
                .filter(v -> v.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
        UUID userId = user.getId();
        return userId;
    }

    private User findUserEntry(String userName) {
        // Stream API 활용해서 엔트리의 값 중 유저 이름을 찾음.
        User user = userData.values().stream()
                .filter(v -> v.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
        return user;
    }
}
