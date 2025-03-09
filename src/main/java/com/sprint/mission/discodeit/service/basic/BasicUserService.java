package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UUID login(String id, String pwd, UUID loginUserKey) {
        UUID userKey = userRepository.findUserKeyById(id);
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
        UUID userKey = userRepository.findUserKeyById(id);
        if (userKey != null && userRepository.existsByKey(userKey)) {
            throw new IllegalArgumentException("[Error] 동일한 사용자가 존재합니다.");
        }
        User user = new User(id, name, pwd, email, phone);
        return userRepository.save(user);
    }

    @Override
    public User read(String id) {
        UUID userKey = userRepository.findUserKeyById(id);
        if (userKey == null) {
            throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
        }
        return userRepository.findByKey(userKey);
    }

    @Override
    public List<User> readAll(List<String> ids) {
        List<UUID> userKeys = userRepository.findUserKeyByIds(ids);
        if (userKeys.isEmpty()) {
            throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
        }
        return userRepository.findAllByKeys(userKeys);
    }

    @Override
    public UUID update(UUID userKey, String id, String pwd, String email, String phone) {
        User currentUser = userRepository.findByKey(userKey);
        if (currentUser == null) {
            throw new IllegalStateException("[Error] 수정이 불가능합니다");
        }
        if (!id.isEmpty()) {
            currentUser.updateId(id);
        }
        if (!pwd.isEmpty()) {
            currentUser.updatePwd(pwd);
        }
        if (!email.isEmpty()) {
            currentUser.updateEmail(email);
        }
        if (!phone.isEmpty()) {
            currentUser.updatePhone(phone);
        }
        return userRepository.save(currentUser).getUuid();
    }

    @Override
    public void delete(UUID userKey) {
        User currentData = userRepository.findByKey(userKey);
        if (currentData == null) {
            throw new IllegalStateException("[Error] 삭제가 불가능합니다");
        }
        userRepository.delete(currentData);
    }

    @Override
    public String getUserName(UUID userKey) {
        if (userKey == null) {
            throw new IllegalArgumentException("[Error] 이름을 찾을 수 없습니다.");
        }
        return userRepository.findUserName(userKey);
    }

    @Override
    public String getUserId(UUID userKey) {
        if (userKey == null) {
            throw new IllegalArgumentException("[Error] ID를 찾을 수 없습니다.");
        }
        return userRepository.findUserId(userKey);
    }

    private boolean isLoginCheck(UUID userKey) {
        return userKey != null;
    }
}
