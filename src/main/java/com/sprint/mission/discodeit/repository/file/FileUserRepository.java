package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class FileUserRepository implements UserRepository {
    private static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", "user");

    private final List<User> userData;

    public FileUserRepository() {
        userData = new ArrayList<>();
        init();
    }

    @Override
    public void save(User user) {
        userData.add(user);
        Path path = DIRECTORY.resolve(user.getId() + ".ser");
        saveToFile(path, user);
    }

    private void init() {
        if (!Files.exists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveToFile(Path path, User user) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<User> load() {
        try (Stream<Path> path = Files.list(DIRECTORY)) {
            return path
                    .map(this::loadFromFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User loadFromFile(Path path) {
        try (FileInputStream fis = new FileInputStream(path.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void remove(User user) {
        try{
            if (user != null && Files.exists(DIRECTORY.resolve(user.getId() + ".ser"))) {
                Files.delete(DIRECTORY.resolve(user.getId() + ".ser"));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
