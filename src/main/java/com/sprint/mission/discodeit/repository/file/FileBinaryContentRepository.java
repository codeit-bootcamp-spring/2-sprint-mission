package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data = new HashMap<>();
    private static final String FILE_NAME = "binaryContent.ser";

    public FileBinaryContentRepository() {
        loadFromFile();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getUuid(), binaryContent);
        saveToFile();

        return binaryContent;
    }

    @Override
    public BinaryContent findByKey(UUID key) {
        return data.get(key);
    }

    @Override
    public List<BinaryContent> findAllByKeys(List<UUID> binaryKeys) {
        return data.values().stream()
                .filter(binaryContent -> binaryKeys.contains(binaryContent.getUuid()))
                .toList();
    }

    @Override
    public boolean existsByKey(UUID binaryKey) {
        return data.containsKey(binaryKey);
    }

    @Override
    public void delete(UUID key) {
        data.remove(key);
        saveToFile();
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
                    if (entry.getKey() instanceof UUID key && entry.getValue() instanceof BinaryContent value) {
                        data.put(key, value);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] 사용자 데이터를 불러오는 중 문제가 발생했습니다.");
        }
    }
}
