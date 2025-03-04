package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.jcf.impl.LinkedListJCFUserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserService implements UserService {
    private static FileUserService instance;
    private final Map<UUID, UserRepository> userTable = new HashMap<>();

    private FileUserService() {

    }

    public static FileUserService getInstance() {
        if (instance == null) {
            instance = new FileUserService();
        }
        return instance;
    }

    //레포지토리 생성
    private UserRepository getUserRepository(UUID id) {
        UserRepository userRepository = userTable.get(id);
        if (userRepository == null) {
            UserRepository repository = new LinkedListJCFUserRepository();
            userTable.put(id, repository);
            userRepository = repository;
        }
        return userRepository;
    }

    @Override
    public Server createServer(String name) {
        return null;
    }

    @Override
    public void addServer(UUID userId, String name) {

    }

    @Override
    public void addServer(UUID userId, Server server) {

    }

    @Override
    public Server getServer(UUID userId, String name) {
        return null;
    }

    @Override
    public void printServer(UUID userId) {

    }

    @Override
    public void printServer(List<Server> list) {

    }

    @Override
    public boolean removeServer(UUID userId) {
        return false;
    }

    @Override
    public boolean removeServer(UUID userId, String targetName) {
        return false;
    }

    @Override
    public boolean removeServer(List<Server> list, String targetName) {
        return false;
    }

    @Override
    public boolean updateServer(UUID userId) {
        return false;
    }

    @Override
    public boolean updateServer(UUID userId, String targetName) {
        return false;
    }

    @Override
    public boolean updateServer(List<Server> list, String targetName) {
        return false;
    }

    @Override
    public boolean updateServer(List<Server> list, String targetName, String replaceName) {
        return false;
    }

    @Override
    public boolean updateServer(UUID userId, String targetName, String replaceName) {
        return false;
    }
}
