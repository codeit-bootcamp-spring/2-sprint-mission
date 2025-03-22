package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.update.UpdateUserRequestDTO;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface UserRepository {

    void reset();

    User save(User user);

    User findById(UUID id);

    List<User> findAll( );

    User update(User user, UpdateUserRequestDTO updateUserRequestDTO, UUID newProfileId);

    UUID remove(User user);

    boolean existId(UUID id);

    boolean existName(String name);

    boolean existEmail(String email);

}
