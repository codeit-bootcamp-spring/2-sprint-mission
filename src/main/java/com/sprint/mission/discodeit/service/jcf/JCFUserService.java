package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.Server.ServerCreateDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerDeleteDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerJoinDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerUpdateDTO;
import com.sprint.mission.discodeit.DTO.User.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.User.UserDeleteDTO;
import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;
import com.sprint.mission.discodeit.Exception.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.UnauthorizedAccessException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JCFUserService implements UserService {
    private final JCFUserRepository userRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            userRepository.reset();
        }
    }

    @Override
    public UUID registerUser(UserCreateDTO userCreateDTO) {
        User user = new User(userCreateDTO.userName(), userCreateDTO.email(), userCreateDTO.password());
        userRepository.save(user);

        System.out.println("✅ 새 유저 등록됨: " + user);

        return user.getId();
    }

    public UUID createServer(ServerCreateDTO serverCreateDTO) {
        UUID UID = UUID.fromString(serverCreateDTO.ownerId());
        User owner;

        owner = userRepository.findUserByUserId(UID);
        Server server = new Server(UID, serverCreateDTO.name());
        userRepository.saveServer(owner, server);
        return server.getServerId();

    }

    @Override
    public UUID joinServer(ServerJoinDTO serverJoinDTO) {
        UUID UID = UUID.fromString(serverJoinDTO.userId());
        UUID UOID = UUID.fromString(serverJoinDTO.ownerId());
        UUID SID = UUID.fromString(serverJoinDTO.serverId());

        User findUser = userRepository.findUserByUserId(UID);
        User ownerUser = userRepository.findUserByUserId(UOID);
        Server findServer = userRepository.findServerByServerId(ownerUser, SID);

        UUID uuid = userRepository.joinServer(findUser,ownerUser,findServer );

        return uuid;
    }

    @Override
    public User findUser(String name) {
        List<User> list = userRepository.findUserList();
        User user = list.stream().filter(u -> u.getName().equals(name))
                .findFirst().orElseThrow(() -> new UserNotFoundException("해당 이름을 가진 유저를 찾을 수 없습니다."));
        return user;
    }

    @Override
    public List<User> findUserAll() {
        return userRepository.findUserList();
    }


    @Override
    public Server findServer(String userId, String name) {
        List<Server> servers = findServerAll(userId);

        Server server = servers.stream().filter(s -> s.getName().equals(name))
                .findFirst().orElseThrow(() -> new UnauthorizedAccessException("서버에 가입해 있지 않습니다."));
        return server;
    }

    @Override
    public List<Server> findServerAll(String ownerId) {
        UUID UID = UUID.fromString(ownerId);
        User owner = userRepository.findUserByUserId(UID);
        List<Server> servers = userRepository.findServerListByOwner(owner);

        return servers;
    }

    @Override
    public void printUser() {
        List<User> list = userRepository.findUserList();
        for (User user : list) {
            System.out.println(user);
        }
    }

    @Override
    public void printServer(String userId) {
        UUID UID = UUID.fromString(userId);
        User findUser = userRepository.findUserByUserId(UID);

        List<Server> serverList = userRepository.findServerListByOwner(findUser);
        for (Server server : serverList) {
            System.out.println(server);
        }
    }

    @Override
    public boolean deleteUser(UserDeleteDTO userDeleteDTO) {
        try {
            UUID UID = UUID.fromString(userDeleteDTO.userId());
            User findUser = userRepository.findUserByUserId(UID);
            userRepository.remove(findUser);
            return true;
        }catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e) {
            System.out.println("유저를 찾지 못했습니다.");
            return false;
        }
    }

    @Override
    public boolean deleteServer(ServerDeleteDTO serverDeleteDTO) {
        try {
            UUID UID = UUID.fromString(serverDeleteDTO.ownerId());
            UUID SID = UUID.fromString(serverDeleteDTO.serverId());
            User findUser = userRepository.findUserByUserId(UID);
            Server findServer = userRepository.findServerByServerId(findUser, SID);
            userRepository.removeServer(findUser, findServer);
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e1) {
            System.out.println("삭제할 유저가 존재하지 않습니다." + serverDeleteDTO.ownerId());
            return false;
        } catch (ServerNotFoundException e2) {
            System.out.println("삭제할 서버가 존재하지 않습니다." + serverDeleteDTO.serverId());
            return false;
        }
    }

    @Override
    public boolean updateUser(UserUpdateDTO userUpdateDTO) {
        try {
            UUID UID = UUID.fromString(userUpdateDTO.userId());
            User findUser = userRepository.findUserByUserId(UID);
            userRepository.updateUserName(findUser, userUpdateDTO.replaceName());
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e1) {
            System.out.println("업데이트할 유저가 존재하지 않습니다." + userUpdateDTO.userId());
            return false;
        }
    }

    @Override
    public boolean updateServer(ServerUpdateDTO serverUpdateDTO) {
        try {
            UUID UID = UUID.fromString(serverUpdateDTO.ownerId());
            UUID SID = UUID.fromString(serverUpdateDTO.serverId());
            User findUser = userRepository.findUserByUserId(UID);
            Server findServer = userRepository.findServerByServerId(findUser, SID);
            userRepository.updateServerName(findUser, findServer,serverUpdateDTO.replaceName());
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e1) {
            System.out.println("업데이트할 유저가 존재하지 않습니다." + serverUpdateDTO.ownerId());
            return false;
        } catch (ServerNotFoundException e2) {
            System.out.println("업데이트할 서버가 존재하지 않습니다." + serverUpdateDTO.serverId());
            return false;
        }
    }
}
