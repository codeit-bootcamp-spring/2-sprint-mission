package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

// FilChannelService나 FileMessageService에서 다른 FileSerivce 의존성 주입 받아야함 -> 다형성을 위해 인터페이스 구현
public interface FileService<T> {
    void saveToFile(T t);
    List<T> loadFromFile();
    void deleteFile(T t);

}
