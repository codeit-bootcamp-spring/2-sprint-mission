package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class FileUserRepository implements UserRepository {
    private final String USER_FILE = "users.ser";
    private final Map<UUID, User> userData;
    private final SaveLoadHandler saveLoadHandler;

    public  FileUserRepository() {
        saveLoadHandler = new SaveLoadHandler<>(USER_FILE);
        userData = saveLoadHandler.loadData();
    }


    @Override
    public User save(User user) {
        userData.put(user.getId(), user);
        saveLoadHandler.saveData(userData);
        return  user;
    }

    @Override
    public User findById(UUID id) {
        return userData.get(id);
    }

    @Override
    public List<User> findAll() {
        return userData.values().stream().toList();
    }

    @Override
    public User update(UUID id, String newUsername, String newEmail, String newPassword) {
        User userNullable = userData.get(id);
        User user = Optional.ofNullable(userNullable).orElseThrow(() -> new NoSuchElementException(id + "가 존재하지 않습니다."));
        user.updateUser(newUsername, newEmail, newPassword);
        saveLoadHandler.saveData(userData);

        return user;
    }

    @Override
    public void delete(UUID id) {
        userData.remove(id);
        saveLoadHandler.saveData(userData);
    }
}
