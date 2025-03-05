package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final String USER_FILE = "users.ser";
    private final Map<UUID, User> data;

    public FileUserService() {
        data = loadData();
    }

    private Map<UUID, User> loadData(){
        File file = new File(USER_FILE);
        if(!file.exists()){
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis)){
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData(){
        try(FileOutputStream fos = new FileOutputStream(USER_FILE);
        ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(this.data);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        this.data.put(user.getId(), user);
        saveData();
        return user;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = this.data.get(userId);
        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User" + userId + "가 존재하지 않습니다."));
    }

    @Override
    public List<User> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User userNullable = this.data.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User" + userId + "가 존재하지 않습니다."));
        user.update(newUsername, newEmail, newPassword);
        saveData();

        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!this.data.containsKey(userId)) {
            throw new NoSuchElementException("User" + userId + "가 존재하지 않습니다.");
        }
        this.data.remove(userId);
        saveData();
    }
}
