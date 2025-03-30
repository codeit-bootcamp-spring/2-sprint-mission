package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

    private final String USER_FILE;
    private final Map<UUID, User> userData;
    private final SaveLoadHandler<User> saveLoadHandler;

    public  FileUserRepository(@Value("${discodeit.repository.file.user}") String fileName, SaveLoadHandler<User> saveLoadHandler) {
        USER_FILE = fileName;
        this.saveLoadHandler = saveLoadHandler;
        userData = saveLoadHandler.loadData(USER_FILE);
    }


    @Override
    public User save(User user) {
        userData.put(user.getId(), user);
        saveLoadHandler.saveData(USER_FILE, userData);
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
        saveLoadHandler.saveData(USER_FILE, userData);

        return user;
    }

    @Override
    public void delete(UUID id) {
        if(!userData.containsKey(id)){
            throw new NoSuchElementException("유저 " + id + "가 존재하지 않습니다.");
        }
        userData.remove(id);
        saveLoadHandler.saveData(USER_FILE, userData);
    }
}
