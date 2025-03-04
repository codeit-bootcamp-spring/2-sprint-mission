package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.*;
import java.util.*;

public interface UserService {
    void createUser(User user);
    User getUser(UUID id);
    List<User> getAllUsers();
    void updateUser(UUID id, String username);
    void deleteUser(UUID id);
}
