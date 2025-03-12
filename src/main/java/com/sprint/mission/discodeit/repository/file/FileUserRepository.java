package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final File file;
    private Map<UUID, User> data;

    public FileUserRepository(String filename) {
        this.file = new File(filename);
        this.data = loadData();
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, User> loadData() {
        if (!file.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        saveData();
    }

    @Override
    public User findById(UUID userId) {
        return Optional.ofNullable(data.get(userId))
                .orElseThrow(() -> new NoSuchElementException("유저 존재하지 않음."));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);
        saveData();
    }
}
