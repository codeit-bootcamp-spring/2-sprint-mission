package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final List<User> userList = new ArrayList<>();

    @Override
    public void save(String nickname, String password) {
        User user = new User(nickname, password);
        userList.add(user);
        System.out.println("유저 생성 완료" + user);
    }

    @Override
    public User findByUser(UUID uuid) {
        for (User u : userList) {
            if (u.getId().equals(uuid)) {
                return u;
            }
        }
        System.out.print("[실패]일치하는 회원이 없습니다.  ");
        return null;
    }

    @Override
    public List<User> findAllUser() {
        if (userList.isEmpty()) {
            System.out.println("아이디가 존재하지 않습니다");
        }
        return userList;
    }

    @Override
    public void update(UUID uuid, String nickname) {
        if (userList.stream().noneMatch(data -> data.getId().equals(uuid))) {
            System.out.println("[실패]수정하려는 아이디가 존재하지 않습니다.");
            return;
        }

        for (User u : userList) {
            if (u.getId().equals(uuid)) {
                u.updateNickname(nickname);
                System.out.println("[성공]사용자 변경 완료[사용자 아이디: " + u.getId() +
                        ", 닉네임: " + u.getNickname() +
                        ", 변경 시간: " + u.getUpdatedAt() +
                        "]");
            }
        }
    }

    @Override
    public void delete(UUID uuid) {
        boolean removed = userList.removeIf(u -> u.getId().equals(uuid));

        if (!removed) {
            System.out.println("[실패]존재하지 않는 사용자");
        } else {
            System.out.println("[성공]사용자 삭제 완료");
        }
    }

    @Override
    public UUID login(UUID userUUID, String password) {
        User userInfo = findByUser(userUUID);
        if (userInfo == null) {
            return null;
        }

        if (!userInfo.getPassword().equals(password)) {
            System.out.println("[실패]비밀번호가 일치하지 않습니다.");
            return null;
        }

        System.out.println("[성공] 로그인 완료");
        return userInfo.getId();
    }
}
