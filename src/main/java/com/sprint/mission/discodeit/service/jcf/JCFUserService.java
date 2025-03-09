package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    protected final Map<UUID, User> data = new HashMap<>();

    public JCFUserService() {}

    @Override
    public UUID login(String id, String pwd, UUID loginUserKey) {
        UUID userKey = getUserKeyBy(id, pwd);
        if (userKey == null) {
            throw new IllegalArgumentException("[Error] 잘못된 ID 또는 비밀번호 입니다.");
        }
        if (isLoginCheck(loginUserKey)) {
            throw new IllegalStateException("[Error] 이미 로그인 되어 있습니다.");
        }
        return userKey;
    }


    @Override
    public void logOut(UUID userKey) {
        if (!isLoginCheck(userKey)) {
            throw new IllegalStateException("[Error] 로그인을 먼저 해주세요.");
        }
    }

    @Override
    public User create(String id, String name, String pwd, String email, String phone) {
        if (isUserCheck(id)) {
            throw new IllegalArgumentException("[Error] 동일한 사용자가 존재합니다.");
        }
        User user = new User(id, name, pwd, email, phone);
        data.put(user.getUuid(), user);
        return user;
    }

    @Override
    public User read(String id) {
        return data.values().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다."));
    }

    @Override
    public List<User> readAll(List<String> id) {
        List<User> users = data.values().stream()
                .filter(u -> id.contains(u.getId()))
                .toList();
        if (users.isEmpty()) {
            throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
        }
        return users;
    }

    @Override
    public UUID update(UUID uuid, String id, String pwd, String email, String phone) {
        User currentData = data.get(uuid);
        if (currentData == null) {
            throw new IllegalStateException("[Error] 수정이 불가능합니다");
        }
        if (!id.isEmpty()) {
            currentData.updateId(id);
        }
        if (!pwd.isEmpty()) {
            currentData.updatePwd(pwd);
        }
        if (!email.isEmpty()) {
            currentData.updateEmail(email);
        }
        if (!phone.isEmpty()) {
            currentData.updatePhone(phone);
        }
        return currentData.getUuid();
    }

    @Override
    public void delete(UUID uuid) {
        User currentData = data.get(uuid);
        if (currentData == null) {
            throw new IllegalStateException("[Error] 삭제가 불가능합니다");
        }
        data.remove(uuid);
    }

    @Override
    public String getUserName(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("[Error] 이름을 찾을 수 없습니다.");
        }
        return data.get(uuid).getName();
    }

    @Override
    public String getUserId(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("[Error] ID를 찾을 수 없습니다.");
        }
        return data.get(uuid).getId();
    }

    private boolean isUserCheck(String id) {
        return data.values().stream()
                .anyMatch(i -> i.getId().equals(id));
    }

    private boolean isLoginCheck(UUID userKey) {
        return userKey != null;
    }

    private UUID getUserKeyBy(String id, String pwd) {
        return data.values().stream()
                .filter(e -> e.getId().equals(id) && e.getPwd().equals(pwd))
                .map(User::getUuid)
                .findFirst()
                .orElse(null);
    }
}
