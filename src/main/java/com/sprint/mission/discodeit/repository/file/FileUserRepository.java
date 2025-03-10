package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "users.ser";

    @Override
    public void save(User user) {
        List<User> users = findAll();
        users.add(user);
        writeToFile(users);
    }

    @Override
    public User findById(UUID id) {
        return findAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeToFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
