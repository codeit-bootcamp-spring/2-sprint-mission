package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final String FILE_PATH = "data/files/users.dat";
    private Map<UUID, User> data = new HashMap<>();

    public FileUserService() {
        loadFromFile();
    }

    @Override
    public void create(User entity) {
        data.put(entity.getId(), entity);
        saveToFile();
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, User entity) {
        if (data.containsKey(id)) {
            entity.update();
            data.put(id, entity);
            saveToFile();
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        saveToFile();
    }

    private void saveToFile() {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile(); // 부모 디렉토리 가져오기

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // 저장할 디렉토리 자동 생성
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            data = new HashMap<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            data = (Map<UUID, User>) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
