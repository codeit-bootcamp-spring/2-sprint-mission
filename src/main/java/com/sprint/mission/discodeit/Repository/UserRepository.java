package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.RequestToService.UserUpdateDTO;
import com.sprint.mission.discodeit.DTO.legacy.User.UserCRUDDTO;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface UserRepository {

    void reset();

    User save(User user);

    User find(UUID userId);

    List<User> findUserList( );

    User update(User user, UserUpdateDTO userCRUDDTO,UUID replaceProfileId);

    UUID remove(User user);

}
