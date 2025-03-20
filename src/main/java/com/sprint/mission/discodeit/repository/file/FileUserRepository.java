package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@Primary
public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "users.ser";

    @Override
    public User save(User user) {
        Map<UUID, User> users = loadUsers();
        users.put(user.getId(), user);
        saveUsers(users);
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        Map<UUID, User> users = loadUsers();
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(loadUsers().values());
    }

    @Override
    public User update(User user) {
        return save(user);
    }

    @Override
    public void delete(User user) {
        Map<UUID, User> users = loadUsers();
        users.remove(user.getId());
        saveUsers(users);
    }

    @Override
    public boolean exists(UUID userId) {
        Map<UUID, User> users = loadUsers();
        return users.containsKey(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        Map<UUID, User> users = loadUsers();
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        Map<UUID, User> users = loadUsers();
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private Map<UUID, User> loadUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("users.ser not found. Generating new one.");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            // 직렬화된 데이터를 로드하여 반환합니다.
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading users. Returning empty map.");
            return new HashMap<>();
        }
    }

    private void saveUsers(Map<UUID, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            // 데이터를 직렬화하여 파일에 저장합니다.
            oos.writeObject(users);
            System.out.println("users saved.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving users.");
        }
    }

    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        Map<UUID, User> users = loadUsers();
        for (User user : users.values()) {
            if ((user.getUsername().equals(username)) || (user.getEmail().equals(email))) {
                return true;
            }
        }
        return false;
    }
}

