package com.sprint.mission.discodeit.core.message.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.core.channel.entity.QChannel;
import com.sprint.mission.discodeit.core.message.MessageException;
import com.sprint.mission.discodeit.core.message.dto.PageResponse;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.entity.QMessage;
import com.sprint.mission.discodeit.core.storage.entity.QBinaryContent;
import com.sprint.mission.discodeit.core.user.entity.QUser;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomMessageRepositoryImpl implements CustomMessageRepository {

  private final JPAQueryFactory queryFactory;
  private final QMessage message = QMessage.message;
  private final QBinaryContent binaryContent = QBinaryContent.binaryContent;
  private final QUser user = QUser.user;
  private final QChannel channel = QChannel.channel;

  @Override
  public Message findByMessageId(UUID id) {
    Message fetched = queryFactory
        .selectFrom(message)
        .leftJoin(message.author, user).fetchJoin()
        .leftJoin(message.channel, channel).fetchJoin()
        .where(message.id.eq(id))
        .fetchOne();
    if (fetched == null) {
      throw new MessageException(ErrorCode.MESSAGE_NOT_FOUND, id);
    }

    return fetched;
  }

  @Override
  public PageResponse<Message> findAllByChannelIdWithAuthor(UUID channelId, Instant cursor,
      Pageable pageable) {
    List<Message> content = queryFactory.selectFrom(message)
        .join(message.author, user).fetchJoin()
        .leftJoin(message.channel, channel).fetchJoin()
        .where(
            message.channel.id.eq(channelId)
//            cursor != null ? message.createdAt.lt(cursor) : null
        )
        .orderBy(getOrderSpecifiers(pageable.getSort()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    boolean hasNext = content.size() > pageable.getPageSize();
    if (hasNext) {
      content.remove(pageable.getPageSize());
    }

    Long totalElements = queryFactory.select(message.count())
        .from(message)
        .where(message.channel.id.eq(channelId))
        .fetchOne();

    Object nextCursor = content.isEmpty() ? null : content.get(content.size() - 1).getCreatedAt();

    return new PageResponse<>(
        content, nextCursor, content.size(), hasNext, totalElements != null ? totalElements : 0L
    );
  }

  @Override
  public List<Message> findByChannelId(UUID channelId) {
    return queryFactory
        .selectFrom(message)
        .leftJoin(message.author, user).fetchJoin()
        .leftJoin(message.channel, channel).fetchJoin()
        .where(message.channel.id.eq(channelId))
        .fetch();
  }

  private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
    return sort.stream()
        .map(order -> {
          Order direction = order.isAscending() ? Order.ASC : Order.DESC;
          return switch (order.getProperty()) {
            case "createdAt" -> new OrderSpecifier<>(direction, message.createdAt);
            case "author.name" -> new OrderSpecifier<>(direction, message.author.name);
            default -> new OrderSpecifier<>(Order.DESC, message.createdAt);
          };
        })
        .toArray(OrderSpecifier[]::new);
  }
}
