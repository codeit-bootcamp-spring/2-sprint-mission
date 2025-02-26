package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private static volatile JCFUserService instance;
    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }

    @Override
    public void createUser(User user) {
        //데이터 유효성 확인
        this.userValidationCheck(user);
        //사용자 확인
        if (this.selectUserById(user.getId()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }
        if (this.selectUserByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (this.selectUserByNickname(user.getNickname()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        //create
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> selectUserById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> selectUserByNickname(String nickname) {
        if (nickname == null) {
            return Optional.empty();
        }
        return data.values().stream()
                .filter(user -> user.getNickname().equals(nickname))
                .findFirst();
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return data.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> selectAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, String password, String nickname, UserStatus status, UserRole role) {
        //데이터 유효성 확인
        if (id == null) {
            throw new IllegalArgumentException("ID 값이 없습니다.");
        }
        //사용자 확인
        User user = this.selectUserById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다. : " + id));
        //update
        if (password != null) {
            if (password.length() < 6) {
                throw new IllegalArgumentException("비밀번호는 최소 6자 이상이어야 합니다.");
            }
            user.updatePassword(password);
        }
        if (nickname != null) {
            if (nickname.length() < 3 || nickname.length() > 20) {
                throw new IllegalArgumentException("닉네임은 3~20자 사이여야 합니다.");
            }
            if (this.selectUserByNickname(nickname).isPresent()) {
                throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
            }
            user.updateNickname(nickname);
        }
        if (status != null) {
            user.updateStatus(status);
        }
        if (role != null) {
            user.updateRole(role);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        //데이터 유효성 확인
        if (id == null) {
            throw new IllegalArgumentException("ID 값이 없습니다.");
        }
        //사용자 확인
        User user = this.selectUserById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다. : " + id));
        //delete
        data.remove(user.getId());
    }

    /****************************
     * User Data Validation check
     ****************************/
    private void userValidationCheck(User user){
        // 1. null check
        if (user == null) {
            throw new IllegalArgumentException("User 객체가 null입니다.");
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("ID 값이 없습니다.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("이메일이 없습니다.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 없습니다.");
        }
        if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임이 없습니다.");
        }
        if (user.getStatus() == null) {
            throw new IllegalArgumentException("Status 값이 없습니다.");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Role 값이 없습니다.");
        }
        //2. 이메일 형식 check
        if (!user.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        //3. 비밀번호 길이 check
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("비밀번호는 최소 6자 이상이어야 합니다.");
        }
        //4. 닉네임 길이 check
        if (user.getNickname().length() < 3 || user.getNickname().length() > 20) {
            throw new IllegalArgumentException("닉네임은 3~20자 사이여야 합니다.");
        }
    }

}
