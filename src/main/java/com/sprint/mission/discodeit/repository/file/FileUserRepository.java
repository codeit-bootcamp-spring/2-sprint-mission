package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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

public class FileUserRepository implements UserRepository {
    private Map<UUID, User> userData;
    private static final String USER_FILE_PATH = "users.ser";

    public FileUserRepository() {
        dataLoad();
    }

    public void dataLoad() {
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

    public void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE_PATH))) {
            oos.writeObject(userData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 저장할 수 없습니다."+ e.getMessage(), e);
        }
    }

    public Map<UUID, User> getUserData() {
        return userData;
    }

    public User save(User user){
        this.userData.put(user.getId(), user);
        dataSave();

        return user;
    }

    public User update(User user, String newUsername, String newEmail, String newPassword){
        user.update(newUsername, newEmail, newPassword);

        dataSave();
        return user;
    }

    public List<User> findAll(){
        return this.userData.values().stream().toList();
    }

    public User findById(UUID userId){
        return Optional.ofNullable(userData.get(userId))
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    public void delete(UUID userId){
        userData.remove(userId);
        dataSave();
    }
}
