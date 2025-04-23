package com.sprint.discodeit.sprint.repository;

import com.sprint.discodeit.sprint.config.UserPrivateChannelMessageFindRepository;
import com.sprint.discodeit.sprint.domain.entity.PrivateChannelUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateChannelUserRepository extends JpaRepository<PrivateChannelUser, Long>,
        UserPrivateChannelMessageFindRepository {
    List<PrivateChannelUser> findAllByUser_Id(Long usersId);


}
