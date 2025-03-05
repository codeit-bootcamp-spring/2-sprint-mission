package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "users.ser";

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null!");
        }

        List<User> users = readFromFile();

        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        writeToFile(users);
    }

    public User findById(UUID id) {
        List<User> users = readFromFile();
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(UUID id) {
        List<User> users = readFromFile();
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            writeToFile(users);
        }
    }

    @Override
    public List<User> findAll() {
        return readFromFile();
    }

    private void writeToFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<User> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            List<User> users = (List<User>) ois.readObject();

            for (User user : users) {
                if (user.getId() == null) {
                    user.setId(UUID.randomUUID());
                }
            }
            return users;
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }



}
