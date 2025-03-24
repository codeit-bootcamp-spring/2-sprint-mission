package com.sprint.mission.discodeit.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUtil {

    public static <T> UUID saveToFile(Path directory, T object, UUID objectId) {
        Path filePath = getFilePath(directory, objectId);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + filePath, e);
        }
        return objectId;
    }

    public static <T> Optional<T> loadFromFile(Path directory, UUID objectId) {
        Path filePath = getFilePath(directory, objectId);
        if (Files.exists(filePath)) {
            try (FileInputStream fis = new FileInputStream(filePath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                T object = (T) ois.readObject();
                return Optional.of(object);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("파일 로드 실패: " + filePath, e);
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
            throw new RuntimeException("디렉토리 로드 실패: " + directory, e);
        }
        return objectList;
    }

    public static void deleteFile(Path directory, UUID objectId) {
        try {
            Path filePath = getFilePath(directory, objectId);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: " + objectId, e);
        }
    }

    public static Path getFilePath(Path directory, UUID id) {
        return directory.resolve(id.toString() + ".ser");
    }

    public static void init(Path directory) {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 생성 실패: " + directory, e);
        }
    }

    public static byte[] loadFileToBytes(Path filePath) {
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("바이너리 파일 로드 실패: " + filePath, e);
        }
    }

    public static List<byte[]> loadAllByteFiles(Path directory) {
        List<byte[]> objectList = new ArrayList<>();
        try {
            // 디렉토리 내 파일 목록을 스트림으로 읽고, 각 파일을 바이트 배열로 변환
            Files.list(directory)
                    .filter(Files::isRegularFile) // 정규 파일만 필터링 (디렉토리는 제외)
                    .forEach(path -> {
                            byte[] fileData = loadFileToBytes(path);
                            objectList.add(fileData);
                    });
        } catch (IOException e) {
            throw new RuntimeException("바이너리 파일 디렉토리 열기 실패: " + directory, e);
        }
        return objectList;
    }

    public static void saveBytesToFile(byte[] data, Path directory, String fileName) {
        try {
            Path filePath = directory.resolve(fileName);  // 파일 이름을 포함한 경로 생성
            Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("바이너리 파일 저장 실패: " + fileName, e);
        }
    }

}
