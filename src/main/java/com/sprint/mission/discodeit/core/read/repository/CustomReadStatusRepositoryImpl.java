package com.sprint.mission.discodeit.core.read.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.core.channel.entity.QChannel;
import com.sprint.mission.discodeit.core.read.ReadStatusException;
import com.sprint.mission.discodeit.core.read.entity.QReadStatus;
import com.sprint.mission.discodeit.core.read.entity.ReadStatus;
import com.sprint.mission.discodeit.core.user.entity.QUser;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomReadStatusRepositoryImpl implements CustomReadStatusRepository {

  private final JPAQueryFactory queryFactory;
  private final QReadStatus readStatus = QReadStatus.readStatus;
  private final QUser user = QUser.user;
  private final QChannel channel = QChannel.channel;

  @Override
  public ReadStatus findByReadStatusId(UUID readStatusId) {
    ReadStatus fetched = queryFactory
        .selectFrom(readStatus)
        .leftJoin(readStatus.user, user).fetchJoin()
        .leftJoin(readStatus.channel, channel).fetchJoin()
        .where(readStatus.id.eq(readStatusId))
        .fetchOne();
    if (fetched == null) {
      throw new ReadStatusException(ErrorCode.READ_STATUS_NOT_FOUND, readStatusId);
    }
    return fetched;
  }

  @Override
  public ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
    return queryFactory
        .selectFrom(readStatus)
        .leftJoin(readStatus.user, user).fetchJoin()
        .leftJoin(readStatus.channel, channel).fetchJoin()
        .where(readStatus.user.id.eq(userId), readStatus.channel.id.eq(channelId))
        .fetchOne();
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return queryFactory
        .selectFrom(readStatus)
        .leftJoin(readStatus.user, user).fetchJoin()
        .leftJoin(readStatus.channel, channel).fetchJoin()
        .where(readStatus.user.id.eq(userId))
        .fetch();
  }

  @Override
  public List<UUID> findChannelIdByUserId(UUID userId) {
    return queryFactory
        .select(readStatus.channel.id)
        .from(readStatus)
        .where(readStatus.user.id.eq(userId))
        .fetch();
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return queryFactory
        .selectFrom(readStatus)
        .leftJoin(readStatus.user, user).fetchJoin()
        .leftJoin(readStatus.channel, channel).fetchJoin()
        .where(readStatus.channel.id.eq(channelId))
        .fetch();
  }

  @Override
  public List<ReadStatus> findAllByChannelIdIn(List<UUID> ids) {
    return queryFactory
        .selectFrom(readStatus)
        .leftJoin(readStatus.user, user).fetchJoin()
        .leftJoin(readStatus.channel, channel).fetchJoin()
        .where(readStatus.channel.id.in(ids))
        .fetch();
  }
}
