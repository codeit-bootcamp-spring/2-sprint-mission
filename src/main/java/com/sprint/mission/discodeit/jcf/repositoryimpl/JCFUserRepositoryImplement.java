package com.sprint.mission.discodeit.jcf.repositoryimpl;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;

import java.util.*;

public class JCFUserRepositoryImplement implements UserRepository {
    private final Map<UUID, User> userRepository; //유저아이디/유저

    public JCFUserRepositoryImplement() {
        userRepository = new HashMap<>();
    }

    @Override
    public Optional<User> findByUser(UUID userId) {
        return Optional.ofNullable(userRepository.get(userId));
    }

    @Override
    public boolean registerUserId(User user) {
        userRepository.put(user.getId(), user);
        return true;
    }

    @Override
    public boolean removeUser(UUID userId) {//유저제거
        return userRepository.remove(userId) != null;
    }

    @Override
    //map  없을 경우 빈 set() 반환
    public Set<UUID> findAllUsers() {
        return new HashSet<>(userRepository.keySet());
    }
}



