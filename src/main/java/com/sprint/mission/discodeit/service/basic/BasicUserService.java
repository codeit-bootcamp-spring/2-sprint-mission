package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Exception.InvalidPasswordException;
import com.sprint.mission.discodeit.Exception.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.UnauthorizedAccessException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            userRepository.reset();
        }
    }

    @Override
    public UUID registerUser(String userName, String password) {
        User user = new User(userName, password);
        userRepository.saveUser(user);

        System.out.println("✅ 새 유저 등록됨: " + user);

        return user.getId();
    }

    @Override
    public boolean loginUser(String userId, String password) {
        UUID UID = UUID.fromString(userId);
        User findUser = userRepository.findUserByUserId(UID);
        boolean login = findUser.getPassword().equals(password);
        if (login == false) {
            throw new InvalidPasswordException("패스워드가 일치하지 않습니다.");
        }
        return login;
    }

    public UUID createServer(String userOwnerId, String name) {
        UUID UID = UUID.fromString(userOwnerId);
        User owner;

        owner = userRepository.findUserByUserId(UID);
        Server server = new Server(UID, name);
        userRepository.saveServer(owner, server);
        return server.getServerId();

    }

    @Override
    public UUID joinServer(String userId, String ownerId, String serverId) {
        UUID UID = UUID.fromString(userId);
        UUID UOID = UUID.fromString(ownerId);
        UUID SID = UUID.fromString(serverId);

        User findUser = userRepository.findUserByUserId(UID);
        User ownerUser = userRepository.findUserByUserId(UOID);
        Server findServer = userRepository.findServerByServerId(ownerUser, SID);

        UUID uuid = userRepository.joinServer(findUser,ownerUser,findServer );

        return uuid;
    }

    @Override
    public Server getServer(String userId, String name) {
        UUID UID = UUID.fromString(userId);
        User findUser = userRepository.findUserByUserId(UID);
        List<Server> serverList = userRepository.findServerListByOwner(findUser);
        Server server = serverList.stream().filter(s -> s.getName().equals(name))
                .findFirst().orElseThrow(() -> new UnauthorizedAccessException("서버에 가입해 있지 않습니다."));

        return server;
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
    public boolean removeUser(String userId) {
        try {
            UUID UID = UUID.fromString(userId);
            User findUser = userRepository.findUserByUserId(UID);
            userRepository.removeUser(findUser);
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
    public boolean removeServer(String ownerId, String serverId) {
        try {
            UUID UID = UUID.fromString(ownerId);
            UUID SID = UUID.fromString(serverId);
            User findUser = userRepository.findUserByUserId(UID);
            Server findServer = userRepository.findServerByServerId(findUser, SID);
            userRepository.removeServer(findUser, findServer);
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e1) {
            System.out.println("삭제할 유저가 존재하지 않습니다." + ownerId);
            return false;
        } catch (ServerNotFoundException e2) {
            System.out.println("삭제할 서버가 존재하지 않습니다." + serverId);
            return false;
        }
    }

    @Override
    public boolean updateUser(String userId, String replaceName) {
        try {
            UUID UID = UUID.fromString(userId);
            User findUser = userRepository.findUserByUserId(UID);
            userRepository.updateUserName(findUser, replaceName);
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e1) {
            System.out.println("업데이트할 유저가 존재하지 않습니다." + userId);
            return false;
        }
    }

    @Override
    public boolean updateServer(String ownerId, String serverId, String replaceName) {
        try {
            UUID UID = UUID.fromString(ownerId);
            UUID SID = UUID.fromString(serverId);
            User findUser = userRepository.findUserByUserId(UID);
            Server findServer = userRepository.findServerByServerId(findUser, SID);
            userRepository.updateServerName(findUser, findServer,replaceName);
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e1) {
            System.out.println("업데이트할 유저가 존재하지 않습니다." + ownerId);
            return false;
        } catch (ServerNotFoundException e2) {
            System.out.println("업데이트할 서버가 존재하지 않습니다." + serverId);
            return false;
        }
    }

}
