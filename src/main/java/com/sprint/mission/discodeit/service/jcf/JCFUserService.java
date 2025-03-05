package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private Map<UUID, User> data;
    private static JCFUserService instance = null;

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public UUID createUser() {
        User user = new User();
        data.put(user.getId(), user);
        System.out.println("사용자가 생성되었습니다: \n" + user);
        return user.getId();
    }

    @Override
    public void searchUser(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("조회하신 사용자가 존재하지 않습니다.");
            return;
        }
        System.out.println("USER: " + data.get(id));

    }

    @Override
    public void searchAllUsers() {
        if (data.isEmpty()) {
            System.out.println("등록된 사용자가 존재하지 않습니다.");
            return;
        }
        for (User user : data.values()) {
            System.out.println("USER: " + user);
        }
    }

    @Override
    public void updateUser(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("업데이트할 사용자가 존재하지 않습니다.");
            return;
        }
        data.get(id).updateTime(System.currentTimeMillis());
        System.out.println(id + " 사용자 업데이트 완료되었습니다.");

    }

    @Override
    public void deleteUser(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("삭제할 사용자가 존재하지 않습니다.");
            return;
        }
        data.remove(id);
        System.out.println(id + " 사용자 삭제 완료되었습니다.");

    }
    public boolean existUser(UUID id){
        return data.containsKey(id);
    }
}
