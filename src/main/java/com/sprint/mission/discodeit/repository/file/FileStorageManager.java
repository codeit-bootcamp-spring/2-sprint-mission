package com.sprint.mission.discodeit.repository.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileStorageManager {

    @Value("${discodeit.repository.file-directory}")
    private String filePath;

    public <T> Map<UUID, T> loadFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath + fileName))) {
            return (ConcurrentHashMap<UUID, T>) ois.readObject();
        } catch (EOFException e) {
            System.out.println("⚠ channels.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
            return new ConcurrentHashMap<UUID, T>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 로드 중 오류 발생", e);
        }
    }

    public <T> void saveFile(String fileName, Map<UUID, T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath + fileName))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("데이터 저장 중 오류 발생", e);
        }
    }
}
