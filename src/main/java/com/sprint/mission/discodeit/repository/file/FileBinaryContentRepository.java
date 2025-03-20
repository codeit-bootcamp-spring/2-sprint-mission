package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@Primary
public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final String FILE_PATH = "binary_content_storage.ser";

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> storage = loadStorage();
        storage.put(binaryContent.getId(), binaryContent);
        saveStorage(storage);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Map<UUID, BinaryContent> storage = loadStorage();
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BinaryContent> findByFileName(String fileName) {
        Map<UUID, BinaryContent> storage = loadStorage();
        return storage.values().stream()
                .filter(content -> content.getFileName().equals(fileName))
                .toList();
    }

    @Override
    public List<BinaryContent> findByContentType(String contentType) {
        Map<UUID, BinaryContent> storage = loadStorage();
        return storage.values().stream()
                .filter(content -> content.getContentType().equals(contentType))
                .toList();
    }

    @Override
    public List<BinaryContent> findAll() {
        return new ArrayList<>(loadStorage().values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, BinaryContent> storage = loadStorage();
        if (storage.containsKey(id)) {
            storage.remove(id);
            saveStorage(storage);
        }
    }

    @Override
    public List<BinaryContent> findAllByIds(List<UUID> ids) {
        Map<UUID, BinaryContent> storage = loadStorage();
        return ids.stream()
                .map(storage::get)
                .filter(Objects::nonNull)
                .toList();
    }

    private Map<UUID, BinaryContent> loadStorage() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, BinaryContent>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveStorage(Map<UUID, BinaryContent> storage) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

