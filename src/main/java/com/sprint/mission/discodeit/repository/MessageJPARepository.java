package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageJPARepository extends JpaRepository<Message, UUID> {

    @Query("select m from Message m where m.channel.id = :channel_id")
    @EntityGraph(attributePaths = {"attachments", "author", "channel"})
    Page<Message> findByChannel_IdEntityGraph(@Param("channel_id") UUID channelId, Pageable pageable);

    @Query("select m from Message m where m.channel.id = :channel_id")
    @EntityGraph(attributePaths = {"attachments", "author", "channel"})
    Page<Message> findByIdEntityGraph(@Param("id") UUID id, Pageable pageable);

    List<Message> findByChannel_Id(UUID channelId);

}
