package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.Server.ServerUpdateDTO;
import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;
import com.sprint.mission.discodeit.Exception.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface UserRepository {

    void reset();

    UUID saveUser(User user);

    UUID saveServer(User user, Server server);

    UUID joinServer(User user, User owner, Server server);

    User findUser(User user);

    User findUserByUserId(UUID userId);

    List<Server> findServerListByOwner(User owner);

    List<User> findUserList( );

    Server findServerByOwner(User owner, Server targetServer);

    Server findServerByServerId(User owner, UUID serverId);

    UUID updateUser(User user, UserUpdateDTO userUpdateDTO);


    UUID updateServer(Server targetServer, ServerUpdateDTO serverUpdateDTO);

    UUID removeUser(User user);

    UUID removeServer(User owner, Server server);


    UUID quitServer(User user, Server server);

}
