package com.sprint.mission.discodeit.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
    private static final String EXTENSION = ".ser";

    public static void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Path resolvePath(Path directory, UUID id) {
        return directory.resolve(id.toString() + EXTENSION);
    }

    public static <T> T save(Path directory, T object, UUID id) {
        Path filePath = resolvePath(directory, id);
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    // 바꾸기
    public static String saveBinaryContent(Path directory, MultipartFile file, UUID id) {
        Path filePath = resolvePath(directory, id);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            byte[] fileData = file.getBytes();
            fos.write(fileData);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Optional<T> findById(Path directory, UUID id, Class<T> clazz) {
        Path filePath = resolvePath(directory, id);
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object object = ois.readObject();
            if (clazz.isInstance(object)) {
                return Optional.of(clazz.cast(object));
            }
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> findAll(Path directory, Class<T> clazz) {
        List<T> objectList = new ArrayList<>();
        try {
            Files.list(directory)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .forEach(path -> {
                        UUID id = UUID.fromString(path.getFileName().toString().replace(EXTENSION, ""));
                        Optional<T> objectNullable = findById(directory, id, clazz);
                        objectNullable.ifPresent(objectList::add);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return objectList;
    }

    public static boolean existsById(Path directory, UUID id) {
        Path filePath = resolvePath(directory, id);
        return Files.exists(filePath);
    }

    public static void delete(Path directory, UUID id) {
        Path filePath = resolvePath(directory, id);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}