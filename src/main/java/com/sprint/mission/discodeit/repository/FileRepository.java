package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository<T> {
    void saveToFile(T t, Path directory);
    List<T> loadAllFromFile(Path directory);
    void deleteFileById(UUID id, Path directory);
}
