package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final String USER_FILE = "users.ser";
    private final Map<UUID, User> userData;

    public FileUserService() {
        userData = loadData();
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
            oos.writeObject(this.userData);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public User create(String userName, String email, String password) {
        User newUser = new User(userName, email, password);
        userData.put(newUser.getId(), newUser);
        saveData();
        return newUser;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = userData.get(userId);

        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다."));
    }

    @Override
    public List<User> findAll() {
        return userData.values().stream().toList();
    }

    @Override
    public User update(UUID userId, String newUserName, String newEmail, String newPassword) {
        User userNullable = userData.get(userId);
        User user = Optional.ofNullable(userNullable).orElseThrow(() -> new NoSuchElementException(userId + "가 존재하지 않습니다."));
        user.updateUser(newUserName, newEmail, newPassword);
        saveData();

        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!userData.containsKey(userId)) {
            throw new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다.");
        }
        userData.remove(userId);
        saveData();
    }

    @Override
    public String toString() {
        return "JCFUserService{" +
                "userData=" + userData +
                '}';
    }
}
