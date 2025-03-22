package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileBinaryContentRepository implements BinaryContentRepository {
    private static final FileBinaryContentRepository instance = new FileBinaryContentRepository();
    private static final String FILE_PATH = "binaryContent.ser";
    private final Map<UUID, BinaryContent> data;

    private FileBinaryContentRepository() {
        this.data = loadData();
    }

    public static FileBinaryContentRepository getInstance() {
        return instance;
    }

    private void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        }
    }

    private Map<UUID, BinaryContent> loadData() {
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

    @Override
    public void save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("BinaryContent 저장 중 오류 발생", e);
        }
    }

    @Override
    public Optional<BinaryContent> getById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> getAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("BinaryContent 삭제 중 오류 발생", e);
        }
    }

    @Override
    public List<BinaryContent> getByUserId(UUID userId) {
        return data.values().stream()
                .filter(file -> userId.equals(file.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> getByMessageId(UUID messageId) {
        return data.values().stream()
                .filter(file -> messageId.equals(file.getMessageId()))
                .collect(Collectors.toList());
    }
}
