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
        return null;
    }

    @Override
    public User find(UUID userId) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        return null;
    }

    @Override
    public void delete(UUID userId) {

    }
}
