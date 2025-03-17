package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface UserRepository {

    void reset();

    UUID save(User user);

    User find(UUID userId);

    List<User> findUserList( );

    UUID update(User user, UserCRUDDTO userCRUDDTO);

    UUID remove(User user);

}
