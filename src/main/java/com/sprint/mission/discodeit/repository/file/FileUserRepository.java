package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static volatile FileUserRepository instance;
    // 사용자 데이터를 저장할 디렉토리 (하위에 각 엔티티별로 시리얼라이즈)
    private final Path path;
    private Map<UUID, User> users;

    private FileUserRepository(Path path) {
        users = new HashMap<>();
        this.path = path;
        init(path.getParent());
        loadUsers();
    }

    public static FileUserRepository getInstance(Path path) {
        if (instance == null) {
            synchronized (FileUserRepository.class) {
                if (instance == null) {
                    instance = new FileUserRepository(path);
                }
            }
        }
        return instance;
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        saveUsers();
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return users.get(userId);
    }

    @Override
    public User findByEmail(String email) {
        return users.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(UUID userId) {
        users.remove(userId);
        saveUsers();
    }

    @Override
    public boolean exists(UUID userId) {
        return users.containsKey(userId);
    }


    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 안 됨");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        if (Files.exists(path)) {
            try (InputStream is = Files.newInputStream(path);
                 ObjectInputStream ois = new ObjectInputStream(is)) {
                Object usersObject = ois.readObject();

                if (usersObject instanceof Map) {
                    users = (Map<UUID, User>) usersObject;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("유저 로드 실패");
            }
        }
    }

    private void saveUsers() {
        try (OutputStream os = Files.newOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("유저 저장 실패");
        }
    }
}
