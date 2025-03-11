package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.util.ValidationUtil;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final UserChannelService userChannelService;
    
    // 싱글톤 인스턴스
    private static UserServiceImplement instance;
    
    // private 생성자로 변경하여 외부에서 인스턴스 생성을 제한
    private UserServiceImplement(
            UserRepository userRepository,
            UserChannelService userChannelService) {
        this.userRepository = userRepository;
        this.userChannelService = userChannelService;
    }
    
    // 싱글톤 인스턴스를 반환하는 정적 메소드
    public static synchronized UserServiceImplement getInstance(
            UserRepository userRepository,
            UserChannelService userChannelService) {
        if (instance == null) {
            instance = new UserServiceImplement(userRepository, userChannelService);
        }
        return instance;
    }

    @Override
    public User findByUserId(UUID userId) {
        ValidationUtil.validateNotNull(userId, "사용자 ID");
        return userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
    }

    @Override
    public Set<UUID> findByAllUsersId() {
        return userRepository.findAllUsers();
    }

    @Override
    public User deleteUser(UUID userId) {
        ValidationUtil.validateNotNull(userId, "사용자 ID");
        
        User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        userChannelService.cleanupUserChannels(userId);
        userRepository.deleteUser(userId);
        return user;
    }
    @Override
    public void createdUser(String email, String password) {
        ValidationUtil.validateNotEmpty(email, "이메일");
        ValidationUtil.validateNotEmpty(password, "비밀번호");

        // 이메일 중복 체크
        checkEmailDuplication(email);

        User user = new User(email, password);
        userRepository.register(user);
    }

    public User updateEmail(UUID userId, String newEmail) {
        ValidationUtil.validateNotNull(userId, "사용자 ID");
        ValidationUtil.validateNotEmpty(newEmail, "이메일");

        // 이메일 중복 체크
        checkEmailDuplication(newEmail);

        User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        user.setEmail(newEmail);

        // 변경된 사용자 정보를 저장
        userRepository.updateUser(user);

        return user;
    }
    @Override
    public User updatePassword(UUID userId, String newPassword) {
        ValidationUtil.validateNotNull(userId, "사용자 ID");
        ValidationUtil.validateNotEmpty(newPassword, "비밀번호");

        User user = userRepository.findByUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        if(user.getPassword().equals(newPassword)){
            throw new IllegalArgumentException("같은 비밀번호로는 변경 불가합니다.");
        }
        // 변경된 사용자 정보를 저장
        userRepository.updateUser(user);
        return user;
    }

    private void checkEmailDuplication(String email) {
        Optional<User> existingUser = userRepository.findAllUsers().stream()
                .map(userRepository::findByUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + email);
        }
    }
}
