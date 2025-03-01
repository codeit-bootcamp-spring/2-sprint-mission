package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private static final JCFUserService INSTANCE = new JCFUserService();
    protected final Map<UUID, User> data;

    private JCFUserService() {
        data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        return INSTANCE;
    }

    @Override
    public UUID login(String id, String pwd) {
        if (findUserUuid(id, pwd) == null) {
            System.out.println("[Error] 일치하는 사용자가 없습니다");
        }
        return findUserUuid(id,pwd);
    }

    @Override
    public void create(String id, String name, String pwd, String email, String phone) {
        if (isUserCheck(id)) {
            System.out.println("[Error] 동일한 사용자 정보가 존재합니다");
            return;
        }
        User user = new User(id, name, pwd, email, phone);
        data.put(user.getUuid(), user);
        System.out.println("[Info] 사용자가 생성 되었습니다.");
    }

    @Override
    public User read(String id) {
        if (!isUserCheck(id)) {
            System.out.println("[Error] 조회할 사용자가 존재하지 않습니다");
        }
        return findUserEntity(id);
    }

    public User read(UUID uuid) {
        return findUserEntity(uuid);
    }

    @Override
    public List<User> readAll(List<String> id) {
        if (!isUserCheck(id)) {
            System.out.println("[Error] 조회할 사용자가 존재하지 않습니다");
        }
        return findUserEntity(id);
    }

    @Override
    public User update(UUID uuid, String id, String pwd, String email, String phone) {
        User currentData = data.get(uuid);

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
        System.out.println("[Info] 정상 업데이트 되었습니다 ");
        return currentData;
    }

    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
        System.out.println("[Info] 사용자가 삭제 되었습니다.");
    }

    @Override
    public String getUserName(UUID uuid) {
        if (uuid == null) {
            return "";
        }
        return findUserName(uuid);
    }

    @Override
    public String getUserId(UUID uuid) {
        if (uuid == null) {
            return "";
        }
        return findUserId(uuid);
    }

    private boolean isUserCheck(String id) {
        return data.values().stream()
                .anyMatch(i -> i.getId().equals(id));
    }

    private boolean isUserCheck(List<String> id) {
        return data.values().stream()
                .anyMatch(i -> id.contains(i.getId()));
    }

    private UUID findUserUuid(String id, String pwd) {
        return data.entrySet().stream()
                .filter(e -> e.getValue().getId().equals(id) && e.getValue().getId().equals(pwd))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private User findUserEntity(String id) {
        return data.values().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private List<User> findUserEntity(List<String> id) {
        return data.values().stream()
                .filter(u -> id.contains(u.getId()))
                .collect(Collectors.toList());
    }

    private User findUserEntity(UUID uuid) {
        return data.get(uuid);
    }
    private String findUserId(UUID uuid) {
        return data.get(uuid).getId();
    }
    private String findUserName(UUID uuid) {
        return data.get(uuid).getName();
    }
}
