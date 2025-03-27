package com.sprint.mission.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FileUtils {
    private FileUtils() {
    }

    public static <U, T> T loadAndSave(Path objectPath, Function<Map<U, T>, T> operation) {
        Map<U, T> objects = loadObjectsFromFile(objectPath);
        T result = operation.apply(objects);
        saveObjectsToFile(objectPath, objects);
        return result;
    }

    public static <U, T> void loadAndSaveConsumer(Path objectPath, Consumer<Map<U, T>> operation) {
        Map<U, T> objects = loadObjectsFromFile(objectPath);
        operation.accept(objects);
        saveObjectsToFile(objectPath, objects);
    }

    public static <U, T> Map<U, T> loadObjectsFromFile(Path filePath) {
        if (Files.notExists(filePath)) {
            return new HashMap<>();
        }

        return deserializeFromFile(filePath);
    }

    public static void init(Path filePath) {
        Path parentDir = filePath.getParent();
        if (parentDir == null && Files.exists(filePath)) {
            return;
        }

        creatDirectory(parentDir);
    }

    public static void deleteImageFileFromPath(Path imageFilePath) {
        try {
            Files.deleteIfExists(imageFilePath);
        } catch (IOException e) {
            throw new UncheckedIOException("프로필이미지 파일을 삭제할 수 없습니다.", e);
        }
    }

    public static void saveImageFileToPath(MultipartFile multipartFile, Path imageFilePath) {
        try {
            init(imageFilePath);
            Files.write(imageFilePath, multipartFile.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException("프로필이미지 파일을 저장할 수 없습니다.", e);
        }
    }

    private static <U, T> Map<U, T> deserializeFromFile(Path filePath) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            Object object = objectInputStream.readObject();
            if (object instanceof Map) {
                return (Map<U, T>) object;
            }

            throw new ClassCastException("잘못된 데이터 타입: Map를 기대했지만, " + object.getClass() + "을(를) 발견했습니다.");
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 불러오는 작업을 실패했습니다", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("파일을 읽었지만, 필요한 클래스를 찾을 수 없습니다");
        }
    }

    private static <U, T> void saveObjectsToFile(Path filePath, Map<U, T> objects) {
        init(filePath);
        serializeObjectToFile(filePath, objects);
    }

    private static <U, T> void serializeObjectToFile(Path filePath, Map<U, T> objects) {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(objects);
        } catch (IOException e) {
            throw new UncheckedIOException("파일에 저장하는 작업을 실패했습니다.", e);
        }
    }


    private static void creatDirectory(Path directory) {
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException("디렉터리 생성 실패: " + directory, e);
        }
    }
}