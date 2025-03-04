package com.sprint.sprint2.discodeit.repository.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractFileRepository<T> {
    private final String filePath;

    protected AbstractFileRepository(String filePath) {
        this.filePath = filePath;
    }

    protected void writeToFile(Map<UUID, T> entity) {
        try (FileOutputStream fos = new FileOutputStream(filePath, true);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(entity);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Map<UUID, T> loadAll() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<>(); // 파일이 없으면 빈 Map 반환
        }
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }



}
