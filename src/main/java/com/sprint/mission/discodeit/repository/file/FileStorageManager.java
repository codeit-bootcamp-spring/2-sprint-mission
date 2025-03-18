package com.sprint.mission.discodeit.repository.file;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileStorageManager {
    public <T> Map<UUID, T> loadFile(String FILE_PATH) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (ConcurrentHashMap<UUID, T>) ois.readObject();
        } catch (EOFException e) {
            System.out.println("⚠ channels.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
            return new ConcurrentHashMap<UUID, T>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 로드 중 오류 발생", e);
        }
    }

    public <T> void saveFile(String FILE_PATH, Map<UUID, T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("데이터 저장 중 오류 발생", e);
        }
    }
}
