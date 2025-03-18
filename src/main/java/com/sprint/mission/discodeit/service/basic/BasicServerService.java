package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.legacy.Server.ServerCRUDDTO;
import com.sprint.mission.discodeit.Exception.Empty.EmptyServerListException;
import com.sprint.mission.discodeit.Exception.NotFound.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.NotFound.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BasicServerService implements ServerService {
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            serverRepository.reset();
        }
    }

    @CustomLogging
    @Override
    public Server create(ServerCRUDDTO serverCRUDDTO) {
        UUID userId = serverCRUDDTO.userId();
        try {
            User owner = userRepository.find(userId);

            Server server = new Server(userId, serverCRUDDTO.name());
            serverRepository.save(server, owner);
            serverRepository.join(server, owner);

            return server;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("create: 유저 저장소에 해당 유저가 존재하지 않습니다.");
        }
    }

    @Override
    @CustomLogging
    public User join(ServerCRUDDTO serverCRUDDTO) {
        UUID serverId = serverCRUDDTO.serverId();
        UUID userId = serverCRUDDTO.userId();
        try {
            User user = userRepository.find(userId);
            Server server = serverRepository.find(serverId);
            serverRepository.join(server, user);
            return user;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("join: 유저 저장소에 해당 유저가 존재하지 않습니다.");
        } catch (ServerNotFoundException e) {
            throw new ServerNotFoundException("join: 서버 저장소에 해당 서버가 존재하지 않습니다.");
        }
    }

    @Override
    public User quit(ServerCRUDDTO serverCRUDDTO) {
        UUID serverId = serverCRUDDTO.serverId();
        UUID userId = serverCRUDDTO.userId();
        try {
            User user = userRepository.find(userId);
            Server server = serverRepository.find(serverId);
            serverRepository.quit(server, user);
            return user;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("join: 유저 저장소에 해당 유저가 존재하지 않습니다.");
        } catch (ServerNotFoundException e) {
            throw new ServerNotFoundException("join: 서버 저장소에 해당 서버가 존재하지 않습니다.");
        }
    }

    @Override
    public Server find(UUID serverId) {
        try {
            Server server = serverRepository.find(serverId);
            return server;
        } catch (ServerNotFoundException e) {
            throw new ServerNotFoundException("find: 서버 저장소에 해당 서버가 존재하지 않습니다.");
        }
    }

    @Override
    public List<Server> findServerAll(UUID ownerId) {
        try {
            List<Server> serverList = serverRepository.findAllByUserId(ownerId);
            return serverList;
        } catch (EmptyServerListException e) {
            throw new EmptyServerListException("findServerAll: 서버 저장소에 서버가 존재하지 않습니다.");
        }
    }

    @Override
    public Server update(UUID serverId, ServerCRUDDTO serverCRUDDTO) {
        try {
            Server server = serverRepository.find(serverId);
            Server update = serverRepository.update(server, serverCRUDDTO);
            return update;
        } catch (ServerNotFoundException e) {
            System.out.println("update: 해당 서버가 존재하지 않습니다.");
            return null;
        }
    }

    @Override
    public boolean delete(ServerCRUDDTO serverCRUDDTO) {
        try {
            UUID serverId = serverCRUDDTO.serverId();
            UUID userId = serverCRUDDTO.userId();

            User user = userRepository.find(userId);
            Server server = serverRepository.find(serverId);
            serverRepository.remove(server, user);
            return true;
        } catch (UserNotFoundException e) {
            System.out.println("delete: 유저를 찾지 못했습니다.");
            return false;
        } catch (ServerNotFoundException e) {
            System.out.println("delete: 서버를 찾지 못했습니다.");
            return false;
        } catch (EmptyServerListException e) {
            System.out.println("delete: 서버 리스트가 비어있습니다.");
            return false;
        }
    }

}
