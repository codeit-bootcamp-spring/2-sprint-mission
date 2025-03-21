package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data = new HashMap<>();
    private static final String FILE_NAME = "readStatus.ser";


    public FileReadStatusRepository() {
        loadFromFile();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getUuid(), readStatus);
        saveToFile();

        return readStatus;
    }

    @Override
    public void delete(UUID readStatusKey) {
        data.remove(readStatusKey);
        saveToFile();
    }

    @Override
    public ReadStatus findByKey(UUID readStatusKey) {
        return data.get(readStatusKey);
    }

    @Override
    public List<ReadStatus> findAllByUserKey(UUID userKey) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserKey().equals(userKey))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelKey(UUID channelKey) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getChannelKey().equals(channelKey))
                .toList();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[Error] 사용자 데이터를 저장하는 중 문제가 발생했습니다.");
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (entry.getKey() instanceof UUID key && entry.getValue() instanceof ReadStatus value) {
                        data.put(key, value);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] 사용자 데이터를 불러오는 중 문제가 발생했습니다.");
        }
    }
}
