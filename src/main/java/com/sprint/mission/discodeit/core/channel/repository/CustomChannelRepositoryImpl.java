package com.sprint.mission.discodeit.core.channel.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.core.channel.ChannelException;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.entity.QChannel;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomChannelRepositoryImpl implements CustomChannelRepository {

  private final JPAQueryFactory queryFactory;
  private final QChannel channel = QChannel.channel;

  @Override
  public List<Channel> findAllByTypeOrIdIn(ChannelType type, List<UUID> ids) {
    return queryFactory
        .selectFrom(channel)
        .where(channel.type.eq(type).or(channel.id.in(ids)))
        .fetch();
  }

  @Override
  public Channel findByChannelId(UUID id) {
    Channel fetched = queryFactory.selectFrom(channel)
        .where(channel.id.eq(id))
        .fetchOne();
    if (fetched == null) {
      throw new ChannelException(ErrorCode.CHANNEL_NOT_FOUND, id);
    }
    return fetched;
  }
}
