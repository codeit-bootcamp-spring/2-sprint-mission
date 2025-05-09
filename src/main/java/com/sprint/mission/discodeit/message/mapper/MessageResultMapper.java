package com.sprint.mission.discodeit.message.mapper;

import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.dto.MessageResult;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@RequiredArgsConstructor
@Component
public class MessageResultMapper {

    private final UserStatusRepository userStatusRepository;

    public MessageResult convertToMessageResult(Message message, User user) {
        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        return MessageResult.fromEntity(message, UserResult.fromEntity(user, userStatus.isOnline(Instant.now())));
    }

}
