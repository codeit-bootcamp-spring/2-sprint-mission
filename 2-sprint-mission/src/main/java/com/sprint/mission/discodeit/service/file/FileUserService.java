package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {

    private static volatile FileUserService instance;
    private final UserRepository userRepository;

    private FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static FileUserService getInstance(UserRepository userRepository) {
        if (instance == null) {
            synchronized (FileUserService.class) {
                if (instance == null) {
                    instance = new FileUserService(userRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public void createUser(User user) {
        //데이터 유효성 확인
        userValidationCheck(user);
        //사용자 중복 확인
        checkUserExists(user);
        //create
        userRepository.createUser(user);
    }

    @Override
    public Optional<User> selectUserById(UUID id) {
        return userRepository.selectUserById(id);
    }

    @Override
    public Optional<User> selectUserByNickname(String nickname) {
        if (nickname == null) {
            return Optional.empty();
        }
        return userRepository.selectUserByNickname(nickname);
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return userRepository.selectUserByEmail(email);
    }

    @Override
    public List<User> selectAllUsers() {
        return userRepository.selectAllUsers();
    }

    @Override
    public void updateUser(UUID id, String password, String nickname, UserStatus status, UserRole role) {
        //데이터 유효성 확인
        validateId(id);
        //사용자 확인
        User user = findUserOrThrow(id);
        //유효성 검증
        if (password != null && !password.equals(user.getPassword())) {
            validatePassword(password);
        }
        if (nickname != null && !nickname.equals(user.getNickname())) {
            validateNickname(nickname);
            checkUserNicknameExists(nickname);
        }
        //update
        if (password != null || nickname != null || status != null || role != null) {
            userRepository.updateUser(id, password, nickname, status, role);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        //데이터 유효성 확인
        validateId(id);
        //사용자 확인
        User user = findUserOrThrow(id);
        //delete
        userRepository.deleteUser(user.getId());
    }

    @Override
    public void clearUsers() {
        userRepository.clearUsers();
    }

    /****************************
     * Validation check
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
        validateEmail(user.getEmail());
        //3. 비밀번호 길이 check
        validatePassword(user.getPassword());
        //4. 닉네임 길이 check
        validateNickname(user.getNickname());
    }

    private void validateEmail(String email){
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validatePassword(String password){
        if (password.length() < 6) {
            throw new IllegalArgumentException("비밀번호는 최소 6자 이상이어야 합니다.");
        }
    }

    private void validateNickname(String nickname){
        if (nickname.length() < 3 || nickname.length() > 20) {
            throw new IllegalArgumentException("닉네임은 3~20자 사이여야 합니다.");
        }
    }

    private void validateId(UUID id){
        if (id == null) {
            throw new IllegalArgumentException("사용자 ID 값이 없습니다.");
        }
    }

    private User findUserOrThrow(UUID id) {
        return selectUserById(id)
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다. : " + id));
    }

    private void checkUserExists(User user) {
        checkUserIdExists(user.getId());
        checkUserEmailExists(user.getEmail());
        checkUserNicknameExists(user.getNickname());
    }

    private void checkUserIdExists(UUID id) {
        if (selectUserById(id).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }
    }

    private void checkUserEmailExists(String email) {
        if (selectUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }

    private void checkUserNicknameExists(String nickname) {
        if (selectUserByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
    }

}
