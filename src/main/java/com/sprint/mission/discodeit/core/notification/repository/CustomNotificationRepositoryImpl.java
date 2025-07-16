package com.sprint.mission.discodeit.core.notification.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.core.notification.NotificationException;
import com.sprint.mission.discodeit.core.notification.entity.Notification;
import com.sprint.mission.discodeit.core.notification.entity.QNotification;
import com.sprint.mission.discodeit.core.user.entity.QUser;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

  private final JPAQueryFactory queryFactory;
  private final QNotification notification = QNotification.notification;
  private final QUser user = QUser.user;

  @Override
  public Notification findByNotificationId(UUID notificationId) {
    Notification fetched = queryFactory.selectFrom(notification)
        .leftJoin(notification.receiver, user).fetchJoin()
        .where(notification.id.eq(notificationId))
        .fetchOne();
    if (fetched == null) {
      throw new NotificationException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
    return fetched;
  }

  @Override
  public List<Notification> findAllByUserId(UUID userId) {
    return queryFactory.selectFrom(notification)
        .leftJoin(notification.receiver, user).fetchJoin()
        .where(notification.receiver.id.eq(userId))
        .fetch();
  }
}
