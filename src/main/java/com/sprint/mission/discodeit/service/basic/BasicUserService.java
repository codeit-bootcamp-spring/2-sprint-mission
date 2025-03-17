package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(String username, String email, String password){
        Map<UUID, User> userData = userRepository.getUserData();
        if(userData.values().stream()
                .anyMatch(user -> user.getUsername().equals(username))){
            throw new IllegalArgumentException("같은 이름을 가진 사람이 있습니다.");
        }
        User user = new User(username, email, password);
        return userRepository.save(user);
    }

    @Override
    public User find(UUID userId){
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword){
        Map<UUID, User> userData = userRepository.getUserData();

        User userNullable = userData.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        return userRepository.update(user, newUsername, newEmail, newPassword);
    }

    @Override
    public void delete(UUID userId){
        Map<UUID, User> userData = userRepository.getUserData();
        if (!userData.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        userRepository.delete(userId);
    }
}
