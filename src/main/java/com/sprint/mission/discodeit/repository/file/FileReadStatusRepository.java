package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.io.*;
import java.util.*;

public class FileReadStatusRepository implements ReadStatusRepository {
    private static final FileReadStatusRepository instance = new FileReadStatusRepository();
    private static final String FILE_PATH = "readStatus.ser";
    private final Map<UUID, ReadStatus> data;

    private FileReadStatusRepository() {
        this.data = loadData();
    }

    public static FileReadStatusRepository getInstance() {
        return instance;
    }

    private void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        }
    }

    private Map<UUID, ReadStatus> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public void save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("ReadStatus 저장 중 오류 발생", e);
        }
    }

    @Override
    public Optional<ReadStatus> getById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> getAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("ReadStatus 삭제 중 오류 발생", e);
        }
    }
}
