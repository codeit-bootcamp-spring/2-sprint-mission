package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository<T> {
    void saveToFile(T t);
    List<T> loadAllFromFile();
    void deleteFileById(UUID id);
}
