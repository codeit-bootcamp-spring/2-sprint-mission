package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final String fileName = "users.ser";
    private final Map<UUID, User> userMap;

    public FileUserRepository() {
        this.userMap = loadUserList();
    }

    public void saveUserList() {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(userMap);
        } catch (IOException e) {
            throw new RuntimeException("데이터를 저장하는데 실패했습니다.", e);
        }
    }

    public Map<UUID, User> loadUserList() {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object userMap = ois.readObject();
            return (Map<UUID, User>) userMap;
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("데이터를 불러오는데 실패했습니다", e);
        }
    }

    @Override
    public User save(User user) {
        this.userMap.put(user.getId(), user);
        saveUserList();
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public User update(User user) {
        this.userMap.put(user.getId(), user);
        saveUserList();
        return user;
    }

    @Override
    public boolean delete(UUID userId) {
        boolean removed = userMap.remove(userId) != null;
        if (removed) {
            saveUserList();
        }
        return removed;
    }
}
