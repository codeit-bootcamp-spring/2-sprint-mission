package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface FileService<T> {
    void saveToFile(T t);
    T loadOneFromFile(UUID id);
    List<T> loadAllFromFile();
    void deleteFile(UUID id);
}
