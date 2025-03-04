package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    public void createUser(User user) { data.put(user.getId(), user); }
    public User getUser(UUID id) { return data.get(id); }
    public List<User> getAllUsers() { return new ArrayList<>(data.values()); }
    public void updateUser(UUID id, String username) {
        if (data.containsKey(id)) data.get(id).updateUsername(username);
    }
    public void deleteUser(UUID id) { data.remove(id); }
}