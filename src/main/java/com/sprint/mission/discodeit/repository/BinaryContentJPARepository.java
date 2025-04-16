package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentJPARepository extends JpaRepository<BinaryContent, UUID> {

    @Query(value = """
    SELECT bc.* FROM users u JOIN binary_contents bc ON u.profile_id = bc.id WHERE u.id = :userId
    """, nativeQuery = true)
    Optional<BinaryContent> findProfileImageByUserId(@Param("userId") UUID userId);

    @Query(value = """
    SELECT bc.*
    FROM messages m
    JOIN message_attachments ma ON ma.message_id = m.id
    JOIN binary_contents bc ON bc.id = ma.attachment_id
    WHERE m.channel_id = :messageId
    """, nativeQuery = true)
    List<BinaryContent> findProfileImageByUserIdList(@Param("messageId") UUID messageId);


}
