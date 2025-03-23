package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserStatusRepository implements UserStatusRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "userStatuss");

    private final FileSerializationUtil fileUtil;

    public FileUserStatusRepository(FileSerializationUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(UserStatus userStatus) {
        fileUtil.<UserStatus>writeObjectToFile(userStatus, FilePathUtil.getFilePath(directory, userStatus.getId()));
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return fileUtil.readObjectFromFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public Optional<List<UserStatus>> findAll() {
        try {
            List<UserStatus> userStatuss = Files.list(directory)
                    .map(fileUtil::<UserStatus>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return Optional.of(userStatuss);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(UserStatus user) {
        save(user);
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        try {
            return Files.list(directory)
                    .map(fileUtil::<UserStatus>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(userStatus -> userStatus.getUserId().equals(userId))
                    .findFirst();
        }catch (IOException e) {
            throw new IllegalArgumentException("파일 읽기 실패");
        }
    }
}
