package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserService implements UserService {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "users");
    private final FileSerializationUtil fileUtil;
    private static FileUserService userService;

    private FileUserService(FileSerializationUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public static FileUserService getInstance(FileSerializationUtil fileUtil) {
        if (userService == null) {
            userService = new FileUserService(fileUtil);
        }
        return userService;
    }

    @Override
    public void create(User user) {
        fileUtil.<User>writeObjectToFile(user, FilePathUtil.getFilePath(directory, user.getId()));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return fileUtil.<User>readObjectFromFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public List<User> findAll() {
        if (Files.exists(directory)) {
            try {
                return Files.list(directory)
                        .map(fileUtil::<User>readObjectFromFile) // Optional<User> 반환
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
            } catch (Exception e) {
                throw new RuntimeException("모든 사용자 파일 읽기에 실패했습니다.", e);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void update(UUID id, String username, String email) {
        User user = findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다: " + id));
        user.setUpdatedAt(System.currentTimeMillis());
        user.setUsername(username);
        user.setEmail(email);
        fileUtil.<User>writeObjectToFile(user, FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }
}
