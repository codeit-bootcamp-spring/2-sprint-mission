package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerCreateDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerDeleteDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerJoinDTO;
import com.sprint.mission.discodeit.DTO.Server.ServerUpdateDTO;
import com.sprint.mission.discodeit.DTO.User.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.User.UserDeleteDTO;
import com.sprint.mission.discodeit.DTO.User.UserFindDTO;
import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;
import com.sprint.mission.discodeit.Exception.DuplicateUserException;
import com.sprint.mission.discodeit.Exception.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.UnauthorizedAccessException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    private void checkDuplicate(String userName, String email) {
        List<User> list = userRepository.findUserList();
        Optional<User> duplicateUser = list.stream()
                .filter(u -> u.getName().equals(userName) || u.getEmail().equals(email)).findFirst();
        duplicateUser.ifPresent(u -> {
            throw new DuplicateUserException("해당 이름, 이메일을 가진 유저가 존재합니다.");
        });
    }

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            userRepository.reset();
        }
    }

    @Override
    public UUID registerUser(UserCreateDTO userCreateDTO) {
        checkDuplicate(userCreateDTO.userName(), userCreateDTO.email());
        User user = new User(userCreateDTO.userName(), userCreateDTO.email(), userCreateDTO.password());

        if (userCreateDTO.binaryContent() != null) {
            user.setProfileId(userCreateDTO.binaryContent().getBinaryContentId());
            BinaryContentCreateDTO binaryContentCreateDTO = new BinaryContentCreateDTO(userCreateDTO.binaryContent());
            binaryContentRepository.save(binaryContentCreateDTO);
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userRepository.saveUser(user);
        userStatusRepository.save(userStatus);
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

        UUID uuid = userRepository.joinServer(findUser, ownerUser, findServer);

        return uuid;
    }

    @Override
    public UserFindDTO findUser(String userId) {
        UUID UID = UUID.fromString(userId);
        User user = userRepository.findUserByUserId(UID);
        UserStatus userStatus = userStatusRepository.find(UID);

        UserFindDTO userFindDTO = new UserFindDTO(
                user.getId(),
                user.getProfileId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                userStatus
                );

        return userFindDTO;
    }
    //이게 맞나?
    //일일이 넣는 것이?
    @Override
    public List<UserFindDTO> findUserAll() {
        List<UserFindDTO> findList = new ArrayList<>();

        List<User> userList = userRepository.findUserList();
        for (User user : userList) {
            UserStatus userStatus = userStatusRepository.find(user.getId());
            UserFindDTO userFindDTO = new UserFindDTO(
                    user.getId(),
                    user.getProfileId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCreatedAt(),
                    user.getUpdatedAt(),
                    userStatus
            );
            findList.add(userFindDTO);
        }
        return findList;
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
            userRepository.removeUser(findUser);
            userStatusRepository.delete(findUser.getId());
            binaryContentRepository.delete(findUser.getId());

            return true;
        } catch (IllegalArgumentException e0) {
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
    public boolean updateUser(String userId, UserUpdateDTO userUpdateDTO) {
        try {
            UUID UID = UUID.fromString(userId);
            User findUser = userRepository.findUserByUserId(UID);
            userRepository.updateUser(findUser, userUpdateDTO);
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e1) {
            System.out.println("업데이트할 유저가 존재하지 않습니다.");
            return false;
        }
    }

    @Override
    public boolean updateServer(ServerDeleteDTO serverDeleteDTO, ServerUpdateDTO serverUpdateDTO) {
        try {
            UUID UID = UUID.fromString(serverDeleteDTO.ownerId());
            UUID SID = UUID.fromString(serverDeleteDTO.serverId());
            User findUser = userRepository.findUserByUserId(UID);
            Server targetServer = userRepository.findServerByServerId(findUser, SID);

            userRepository.updateServer(targetServer, serverUpdateDTO);
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e1) {
            System.out.println("업데이트할 유저가 존재하지 않습니다." + serverDeleteDTO.ownerId());
            return false;
        } catch (ServerNotFoundException e2) {
            System.out.println("업데이트할 서버가 존재하지 않습니다." + serverDeleteDTO.serverId());
            return false;
        }
    }
}
