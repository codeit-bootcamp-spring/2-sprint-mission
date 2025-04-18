package com.sprint.mission.discodeit.repository.impl;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

  private final EntityManager em;

  @Override
  public Slice<Message> findByChannelIdBeforeCursor(UUID channelId, Instant cursor,
      Pageable pageable) {
    String direction = pageable.getSort().getOrderFor("createdAt").isAscending() ? ">" : "<";

    String jpql = "SELECT m FROM Message m WHERE m.channel.id = :channelId ";
    if (cursor != null) {
      jpql += "AND m.createdAt " + direction + " :cursor ";
    }
    jpql += "ORDER BY m.createdAt " + (direction.equals(">") ? "ASC" : "DESC");

    TypedQuery<Message> query = em.createQuery(jpql, Message.class)
        .setParameter("channelId", channelId)
        .setMaxResults(pageable.getPageSize());

    if (cursor != null) {
      query.setParameter("cursor", cursor);
    }

    List<Message> content = query.getResultList();
    boolean hasNext = content.size() > pageable.getPageSize();

    if (hasNext) {
      content = content.subList(0, pageable.getPageSize());
    }

    return new SliceImpl<>(content, pageable, hasNext);
  }
}
