package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final String fileName = "users.ser";
    private final Map<UUID, User> userList;

    public FileUserService() {
        this.userList = loadDataFromUserList();
    }

    // 직렬화
    public void saveUserList() {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(userList);
        } catch (IOException e) {
            throw new RuntimeException("데이터를 저장하는데 실패했습니다.", e);
        }
    }

    // 역직렬화
    public Map<UUID, User> loadDataFromUserList() {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object userList = ois.readObject();
            return (Map<UUID, User>) userList;
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("데이터를 불러오는데 실패했습니다", e);
        }
    }

    @Override
    public User create(String userName, String userEmail, String userPassword) {
        User user = new User(userName, userEmail, userPassword);
        this.userList.put(user.getId(), user);
        saveUserList();
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public User findByUserId(UUID userId) {
        User userNullable = this.userList.get(userId);
        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User userNullable = this.userList.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
        user.update(newUsername, newEmail, newPassword);
        saveUserList();
        return user;
    }


    @Override
    public void delete(UUID userId) {
        User removedUser = this.userList.remove(userId);
        if (removedUser == null) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다 : " + userId);
        }
        saveUserList();
    }
}
