package com.sprint.mission.discodeit.core.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.core.storage.entity.QBinaryContent;
import com.sprint.mission.discodeit.core.user.entity.QUser;
import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final QUser user = QUser.user;
  private final QBinaryContent binaryContent = QBinaryContent.binaryContent;

  @Override
  @Transactional(readOnly = true)
  public List<User> findALlFromDB() {
    return jpaQueryFactory.selectFrom(user)
        .leftJoin(user.profile, binaryContent).fetchJoin().fetch();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByUserId(UUID id) {
    User fetched = jpaQueryFactory
        .selectFrom(user)
        .leftJoin(user.profile, binaryContent)
        .fetchJoin()
        .where(user.id.eq(id))
        .fetchOne();

    return Optional.ofNullable(fetched);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByUserName(String name) {
    User fetched = jpaQueryFactory
        .selectFrom(user)
        .leftJoin(user.profile, binaryContent)
        .fetchJoin()
        .where(user.name.eq(name))
        .fetchOne();
    return Optional.ofNullable(fetched);
  }
}
