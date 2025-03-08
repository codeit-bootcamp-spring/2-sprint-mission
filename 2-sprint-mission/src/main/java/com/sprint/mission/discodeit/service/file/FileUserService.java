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
        //id 유효성 확인
        validateId(id);
        //사용자 확인
        User user = findUserOrThrow(id);
        //사용자 닉네임 확인
        checkUserNicknameExists(nickname);
        //update
        userRepository.updateUser(id, password, nickname, status, role);
    }

    @Override
    public void deleteUser(UUID id) {
        //id 유효성 확인
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

