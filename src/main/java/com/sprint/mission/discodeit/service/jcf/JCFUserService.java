package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final JCFUserRepository userRepository;

    public JCFUserService() {
        this.userRepository = new JCFUserRepository();
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User(userDTO.username());
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID id, String newUsername) {
        User user = getUser(id);
        user.updateUsername(newUsername);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        getUser(id);
        userRepository.delete(id);
    }
}
