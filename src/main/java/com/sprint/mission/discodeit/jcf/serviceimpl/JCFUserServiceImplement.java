package com.sprint.mission.discodeit.jcf.serviceimpl;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Set;
import java.util.UUID;

public class JCFUserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final UserChannelService userChannelService;

    public JCFUserServiceImplement(
            UserRepository userRepository,
            ValidationService validationService,
            UserChannelService userChannelService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
        this.userChannelService = userChannelService;
    }

    @Override
    public void createdUser(String email, String password) {
        validationService.validateString(email, "이메일");
        validationService.validateString(password, "비밀번호");
        User user = new User(email, password);
        userRepository.registerUserId(user);
    }

    @Override
    public User findByUserId(UUID userId) {
        return validationService.validateUserExists(userId);
    }

    @Override
    public Set<UUID> findByAllUsersId() {
        return userRepository.findAllUsers();
    }

    @Override
    public User updateEmail(UUID userId, String newEmail) {
        User user = validationService.validateUserExists(userId);
        validationService.validateString(newEmail, "이메일");
        user.setEmail(newEmail);
        return user;
    }

    @Override
    public User updatePassword(UUID userId, String newPassword) {
        User user = validationService.validateUserExists(userId);
        validationService.validateString(newPassword, "비밀번호");
        user.setPassword(newPassword);
        return user;
    }

    @Override
    public User deleteUser(UUID userId) {
        User user = validationService.validateUserExists(userId);
        userChannelService.cleanupUserChannels(userId);
        userRepository.removeUser(userId);
        return user;
    }
}
