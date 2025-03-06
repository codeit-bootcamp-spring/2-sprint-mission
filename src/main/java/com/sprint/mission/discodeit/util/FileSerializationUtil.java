package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileSerializationUtil {
    private static FileSerializationUtil fileserializationUtil;

    private FileSerializationUtil() {}

    public static FileSerializationUtil getInstance() {
        if(fileserializationUtil == null){
            fileserializationUtil = new FileSerializationUtil();
        }
        return fileserializationUtil;
    }

    public <T extends BaseEntity> void writeObjectToFile(T t, Path filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(t);
        } catch (IOException e) {
            throw new RuntimeException("파일 쓰기 실패했습니다: " + filePath, e);
        }
    }

    public <T extends BaseEntity> Optional<T> readObjectFromFile(Path filePath) {
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            T obj = (T) ois.readObject();
            return Optional.ofNullable(obj);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 읽기 실패했습니다: " + filePath, e);
        }
    }

    public void deleteFile(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("File deleted: " + filePath);
            } else {
                throw new RuntimeException("파일이 존재하지 않습니다: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패했습니다: " + filePath, e);
        }
    }
}
