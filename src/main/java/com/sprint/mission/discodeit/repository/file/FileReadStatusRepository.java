package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", "readstatus");

    public FileReadStatusRepository() {
        init();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path path = DIRECTORY.resolve(readStatus.getId() + ".ser");
        saveToFile(path, readStatus);
        return readStatus;
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

    private void saveToFile(Path path, ReadStatus readStatus) {
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(readStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<ReadStatus> loadToId(UUID id) {
        Path path = DIRECTORY.resolve(id + ".ser");
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object obj = ois.readObject();
            if (obj instanceof ReadStatus) {
                return Optional.of((ReadStatus) obj);
            }
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<ReadStatus> load() {
        try (Stream<Path> path = Files.list(DIRECTORY)) {
            return path
                    .map(this::loadFromFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ReadStatus loadFromFile(Path path) {
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return (ReadStatus) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void remove(ReadStatus readStatus) {
        try {
            if (Files.exists(DIRECTORY.resolve(readStatus.getId() + ".ser"))) {
                Files.delete(DIRECTORY.resolve(readStatus.getId() + ".ser"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
