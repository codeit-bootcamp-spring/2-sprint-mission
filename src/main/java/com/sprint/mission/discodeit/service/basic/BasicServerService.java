package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Server.*;
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
        User owner;

        owner = userRepository.find(userId);
        Server server = new Server(userId, serverCRUDDTO.name());
        serverRepository.save(server, owner);
        serverRepository.join(server, owner);

        return server;
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
            System.out.println("해당 유저는 존재하지 않습니다.");
            throw new UserNotFoundException("유저 참여 실패");
        } catch (ServerNotFoundException e) {
            System.out.println("해당 서버는 존재하지 않습니다.");
            throw new ServerNotFoundException("서버 참여 실패");
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
            System.out.println("해당 유저는 존재하지 않습니다.");
            throw new UserNotFoundException("유저 탈퇴 실패");
        } catch (ServerNotFoundException e) {
            System.out.println("해당 서버는 존재하지 않습니다.");
            throw new ServerNotFoundException("서버 탈퇴 실패");
        }
    }

    @Override
    public Server find(String serverId) {
        UUID targetServerId = UUID.fromString(serverId);
        Server server = serverRepository.find(targetServerId);
        return server;
    }

    @Override
    public List<Server> findServerAll(String ownerId) {
        UUID userId = UUID.fromString(ownerId);
        List<Server> serverList = serverRepository.findAllByUserId(userId);
        return serverList;
    }

    @Override
    public void print(String userId) {
        List<Server> servers = findServerAll(userId);
        servers.forEach(System.out::println);
    }

    @Override
    public boolean delete(ServerCRUDDTO serverCRUDDTO) {

        UUID serverId = serverCRUDDTO.serverId();
        UUID userId = serverCRUDDTO.userId();


        User user = userRepository.find(userId);
        Server server = serverRepository.find(serverId);
        serverRepository.remove(server,user);
        return true;
    }

    @Override
    public Server update(String serverId, ServerCRUDDTO serverCRUDDTO) {
        UUID targetServerId = UUID.fromString(serverId);

        Server server = serverRepository.find(targetServerId);
        Server update = serverRepository.update(server, serverCRUDDTO);
        return update;
    }

}
