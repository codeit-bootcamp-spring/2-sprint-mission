package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final File file;
    private final Map<UUID, User> data;

    public FileUserService(String filename) {
        this.file = new File(filename);
        this.data = loadData();
    }

    private void saveData() {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, User> loadData() {
        if (!file.exists()) {
            // 기존 데이터가 없어서 새로운 데이터 객체 생성
            return new HashMap<>();
        }

        // 기존 데이터가 있어서, 읽어옴
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object data = ois.readObject();
            return (Map<UUID, User>) data;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public User create(String username, String password) {
        User user = new User(username, password);
        data.put(user.getId(), user);
        saveData();
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return Optional.ofNullable(data.get(userId))
                .orElseThrow(() -> new NoSuchElementException(userId + "가 존재하지 않음."));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User updateName(UUID userId, String newUsername) {
        User user = findById(userId);
        user.updateName(newUsername);
        saveData();
        return user;
    }

    @Override
    public User updatePassword(UUID userId, String newPassword) {
        User user = findById(userId);
        user.updatePassword(newPassword);
        saveData();
        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!data.containsKey(userId)) {
            throw new NoSuchElementException(userId + " 삭제할 대상이 존재하지 않음.");
        }
        data.remove(userId);
        saveData();
    }
}
