package com.sprint.discodeit.sprint.repository;

import com.sprint.discodeit.sprint.domain.entity.Message;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Slice<Message> findByChannelIdOrderByCreatedAtDesc(Long channelId, Pageable pageable);

    @Query("select m from Message as m where m.channel.id = :channenlId and (: lastMessageId is null or m.id < :lastMessageId)"
            + "order by  m.id DESC ")
    List<Message> findByChannelIdWithCursorPaging( @Param("channelId") Long channelId,
                                                   @Param("lastMessageId") Long lastMessageId,
                                                   Pageable pageable);
}
