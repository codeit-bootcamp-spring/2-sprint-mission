package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUserRepository implements UserRepository {

    private static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", "user");

    public FileUserRepository() {
        FileUtil.init(DIRECTORY);
    }

    @Override
    public void createUser(User user) {
        FileUtil.saveToFile(DIRECTORY, user, user.getId());
    }

    @Override
    public Optional<User> selectUserById(UUID id) {
        return FileUtil.loadFromFile(DIRECTORY, id);
    }

    @Override
    public Optional<User> selectUserByNickname(String nickname) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof User)
                .map(object -> (User) object)
                .filter(user -> user.getNickname().equals(nickname))
                .findFirst();
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof User)
                .map(object -> (User) object)
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> selectAllUsers() {
        return FileUtil.loadAllFiles(DIRECTORY);
    }

    @Override
    public void updateUser(UUID id, String password, String nickname, UserStatus status, UserRole role) {
        FileUtil.loadFromFile(DIRECTORY, id).ifPresent(object -> {
            if (object instanceof User user) {
                user.update(password, nickname, status, role);
                FileUtil.saveToFile(DIRECTORY, user, id);
            } else {
                throw new IllegalArgumentException("User 타입의 객체가 아닙니다. " + id);
            }
        });
    }

    @Override
    public void deleteUser(UUID id) {
        FileUtil.deleteFile(DIRECTORY, id);
    }

    /// ///
    public void clearUsers() {
        try (Stream<Path> paths = Files.walk(DIRECTORY)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);  // 파일 삭제
                            System.out.println(path + " 파일이 삭제되었습니다.");
                        } catch (IOException e) {
                            System.err.println("파일 삭제 중 오류 발생: " + e.getMessage());
                        }
                    });
            System.out.println("모든 사용자 데이터가 초기화되었습니다.");
        } catch (IOException e) {
            System.err.println("파일 접근 중 오류 발생: " + e.getMessage());
        }
    }

}
