package com.sprint.discodeit.sprint.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.discodeit.sprint.domain.entity.Message;
import com.sprint.discodeit.sprint.domain.entity.PrivateChannelUser;
import com.sprint.discodeit.sprint.domain.entity.QMessage;
import com.sprint.discodeit.sprint.domain.entity.QPrivateChannelUser;
import com.sprint.discodeit.sprint.domain.entity.QUsers;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPrivateChannelMessageFindRepositoryImpl implements UserPrivateChannelMessageFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Message> findMessagesByChannelGroupedByUser(Long channelId) {
        QPrivateChannelUser privateChannelUser = QPrivateChannelUser.privateChannelUser;
        QUsers users = QUsers.users;
        QMessage message = QMessage.message;

        return queryFactory
                .selectFrom(message)
                .join(message.users, users)
                .join(privateChannelUser).on(privateChannelUser.user.eq(users))
                .where(privateChannelUser.privateChannel.id.eq(channelId))
                .orderBy(users.id.asc(), message.createdAt.asc())
                .fetch();
    }
}
