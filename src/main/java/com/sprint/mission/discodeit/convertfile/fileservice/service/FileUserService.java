package com.sprint.mission.discodeit.convertfile.fileservice.service;

import com.sprint.mission.discodeit.convertfile.entity.User;
import com.sprint.mission.discodeit.convertfile.fileservice.UserService;
import com.sprint.mission.discodeit.convertfile.fileservice.service.FileUserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private Map<UUID, User> data;
    private final String FILE_PATH = "users.dat";

    public FileUserService() {
        this.data = new HashMap<>();
        loadFromFile();
    }

    // 파일에서 데이터 로드
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object readObject = ois.readObject();
            if (readObject instanceof HashMap) {
                this.data = (HashMap<UUID, User>) readObject;
            }
        } catch (FileNotFoundException e) {
            // 파일이 없는 경우 무시하고 새로운 Map 사용
            this.data = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user data: " + e.getMessage());
            this.data = new HashMap<>();
        }
    }

    // 파일에 데이터 저장
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this.data);
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        this.data.put(user.getId(), user);
        saveToFile();
        return user;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = this.data.get(userId);
        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User userNullable = this.data.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        user.update(newUsername, newEmail, newPassword);
        saveToFile();
        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!this.data.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        this.data.remove(userId);
        saveToFile();
    }
}