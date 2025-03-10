package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
    private Map<UUID, User> userData;
    private static final String USER_FILE_PATH = "users.ser";

    public FileUserService() {
        dataLoad();
    }

    private void dataLoad() {
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            userData = new HashMap<>();
            dataSave();
            return;
        }
        try (FileInputStream fis = new FileInputStream(USER_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            userData = (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 불러올 수 없습니다.");
        }
    }

    private void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE_PATH))) {
            oos.writeObject(userData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 저장할 수 없습니다."+ e.getMessage(), e);
        }
    }

    @Override
    public User create(String username, String email, String password) {
        if(userData.values().stream()
                .anyMatch(user -> user.getUsername().equals(username))){
            throw new RuntimeException("같은 이름을 가진 사람이 있습니다.");
        }
        User user = new User(username, email, password);
        this.userData.put(user.getId(), user);

        dataSave();
        return user;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = this.userData.get(userId);

        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return this.userData.values().stream().toList();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User userNullable = this.userData.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        user.update(newUsername, newEmail, newPassword);

        dataSave();
        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!this.userData.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        this.userData.remove(userId);

        dataSave();
    }
}
