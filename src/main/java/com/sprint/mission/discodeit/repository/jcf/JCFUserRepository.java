package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user, String newUsername, String newEmail, String newPassword, UUID newProfileId){
        user.update(newUsername, newEmail, newPassword, newProfileId);

        return user;
    }

    @Override
    public List<User> findAll(){
        return this.data.values().stream().toList();
    }

    @Override
    public User findById(UUID userId){
        return Optional.ofNullable(data.get(userId)).orElseThrow(()->new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public void delete(UUID userId){
        data.remove(userId);
    }
}
