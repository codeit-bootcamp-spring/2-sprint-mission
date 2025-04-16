package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageJPARepository extends JpaRepository<Message, UUID> {

    @Query("select m from Message m join fetch m.author join fetch m.channel where m.channel.id = :channel_id")
    @EntityGraph(attributePaths = "attachments")
    List<Message> findByChannel_IdEntityGraph(@Param("channel_id") UUID channelId);

    @Query("select m from Message m join fetch m.author join fetch m.channel where m.id = :id")
    @EntityGraph(attributePaths = "attachments")
    Optional<Message> findByIdEntityGraph(@Param("id") UUID id);

    Optional<Message> findByChannel_Id(UUID channelId);
}
