package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.nio.file.Path;
import java.util.List;

public interface UserRepository {


    void save(User user);
    List<User> load();
    void deleteFromFile(User user);

}
