package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

    public class FileUserRepository implements UserRepository {
        private static final Path directoryPath = Paths.get("data/users");
        private static FileUserRepository instance = null;

        public static synchronized FileUserRepository getInstance() {
            if (instance == null) {
                instance = new FileUserRepository();
            }
            return instance;
        }

        private FileUserRepository() {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리를 생성할 수 없습니다: " + e.getMessage());
            }
        }

        private Path getFilePath(UUID userId) {
            return directoryPath.resolve("user-" + userId + ".data");
        }

        private User loadUser(Path filePath) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                return (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("사용자 데이터 읽기 실패: " + filePath, e);
            }
        }

        @Override
        public void save(User user) {
            Path filePath = getFilePath(user.getId());
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
                oos.writeObject(user);
            } catch (IOException e) {
                throw new RuntimeException("사용자 저장 실패: " + user.getId(), e);
            }
        }

        @Override
        public User findById(UUID id) {
            Path filePath = getFilePath(id);
            if (Files.exists(filePath)) {
                return loadUser(filePath);
            }
            return null;
        }

        @Override
        public List<User> findAll() {
            try {
                return Files.list(directoryPath)
                        .filter(Files::isRegularFile)
                        .map(this::loadUser)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException("사용자 목록을 불러오는 중 오류 발생", e);
            }

        }

        @Override
        public void delete(UUID id) {
            Path filePath = getFilePath(id);
            try {
                Files.deleteIfExists(filePath);
                System.out.println(id + " 사용자 삭제 완료되었습니다.");
            } catch (IOException e) {
                throw new RuntimeException("사용자 삭제 실패: " + id, e);
            }
        }

        @Override
        public void update(User user) {
            user.updateTime(System.currentTimeMillis());
            save(user);
            System.out.println(user.getId() + " 사용자 업데이트 완료되었습니다.");
        }
        @Override
        public boolean existsById(UUID id) {
            return Files.exists(getFilePath(id));
        }

        @Override
        public boolean existsByUsername(String username) {
            try {
                return Files.list(directoryPath)
                        .filter(Files::isRegularFile)
                        .map(this::loadUser)
                        .anyMatch(user -> user.getUsername().equals(username));
            } catch (IOException e) {
                throw new RuntimeException("사용자 목록을 가져오는 중 오류 발생", e);
            }
        }
    }
