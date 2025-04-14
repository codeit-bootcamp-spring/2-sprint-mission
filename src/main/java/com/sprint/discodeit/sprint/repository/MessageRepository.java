package com.sprint.discodeit.sprint.repository;

import com.sprint.discodeit.sprint.domain.entity.Message;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message as m WHERE m.channel.id = :channelId")
    List<Message> findByChannelId(@Param("channelId") Long channelId);
}
