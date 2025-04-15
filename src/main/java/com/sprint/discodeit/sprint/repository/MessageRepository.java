package com.sprint.discodeit.sprint.repository;

import com.sprint.discodeit.sprint.domain.entity.Message;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message as m WHERE m.channel.id = :channelId")
    List<Message> findByChannelsId(@Param("channelId") Long channelId);

    Slice<Message> findByChannelIdOrderByCreatedAtDesc(Long channelId, Pageable pageable);
}
