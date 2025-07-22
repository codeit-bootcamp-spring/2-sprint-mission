package com.sprint.mission.discodeit.core.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.core.storage.entity.QBinaryContent;
import com.sprint.mission.discodeit.core.user.UserException;
import com.sprint.mission.discodeit.core.user.entity.QUser;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserRepositoryImpl implements CustomUserRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final QUser user = QUser.user;
  private final QBinaryContent binaryContent = QBinaryContent.binaryContent;

  @Override
  public List<User> findAllFromDB() {
    return jpaQueryFactory
        .selectFrom(user)
        .leftJoin(user.profile, binaryContent)
        .fetchJoin()
        .fetch();
  }

  @Override
  public User findByUserId(UUID id) {
    User fetched = jpaQueryFactory
        .selectFrom(user)
        .leftJoin(user.profile, binaryContent)
        .fetchJoin()
        .where(user.id.eq(id))
        .fetchOne();

    if (fetched == null) {
      throw new UserException(ErrorCode.USER_NOT_FOUND, id);
    }

    return fetched;
  }

  @Override
  public User findByUserName(String name) {
    User fetched = jpaQueryFactory
        .selectFrom(user)
        .leftJoin(user.profile, binaryContent)
        .fetchJoin()
        .where(user.name.eq(name))
        .fetchOne();

    if (fetched == null) {
      throw new UserException(ErrorCode.USER_NOT_FOUND, name);
    }

    return fetched;
  }
}
