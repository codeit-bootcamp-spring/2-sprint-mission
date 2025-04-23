package com.sprint.discodeit.sprint.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.discodeit.sprint.domain.entity.Message;
import com.sprint.discodeit.sprint.domain.entity.QMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPrivateChannelMessageFindRepositoryImpl implements UserPrivateChannelMessageFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Message> findMessagesByChannelGroupedByUser(Long userId, List<Long> channelIds) {
        QMessage message = QMessage.message;

        return queryFactory
                .selectFrom(message)
                .where(message.channel.id.in(channelIds),
                        message.users.id.eq(userId))
                .orderBy(message.createdAt.asc())
                .fetch();
    }
}
