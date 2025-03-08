package com.sprint.mission.discodeit.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUtil {

    public static <T> void saveToFile(Path directory, T object, UUID objectId) {
        Path filePath = getFilePath(directory, objectId);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> Optional<T> loadFromFile(Path directory, UUID objectId) {
        Path filePath = getFilePath(directory, objectId);
        if (Files.exists(filePath)) {
            try (FileInputStream fis = new FileInputStream(filePath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                T object = (T) ois.readObject();
                return Optional.of(object);  // 파일에서 채널 읽어오기
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public static <T> List<T> loadAllFiles(Path directory) {
        List<T> objectList = new ArrayList<>();
        try {
            Files.list(directory)
                    .filter(path -> path.toString().endsWith(".ser"))
                    .forEach(path -> {
                        UUID objectId = UUID.fromString(path.getFileName().toString().replace(".ser", ""));
                        Optional<T> objectOpt = loadFromFile(directory, objectId);
                        objectOpt.ifPresent(objectList::add);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectList;
    }

    public static void deleteFile(Path directory, UUID objectId) {
        Path filePath = getFilePath(directory, objectId);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getFilePath(Path directory, UUID id) {
        return directory.resolve(id.toString() + ".ser");
    }

    public static void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
