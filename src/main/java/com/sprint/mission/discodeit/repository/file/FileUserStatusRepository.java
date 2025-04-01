package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", "userstatus");

    public FileUserStatusRepository() {
        init();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = DIRECTORY.resolve(userStatus.getId() + ".ser");
        saveToFile(path, userStatus);
        return userStatus;
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

    private void saveToFile(Path path, UserStatus userstatus) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(userstatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<UserStatus> load() {
        try (Stream<Path> path = Files.list(DIRECTORY)) {
            return path
                    .map(this::loadFromFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private UserStatus loadFromFile(Path path) {
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (UserStatus) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void remove(UserStatus userStatus) {
        try {
            if (Files.exists(DIRECTORY.resolve(userStatus.getId() + ".ser"))) {
                Files.delete(DIRECTORY.resolve(userStatus.getId() + ".ser"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
