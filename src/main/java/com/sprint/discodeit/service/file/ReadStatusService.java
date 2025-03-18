package com.sprint.discodeit.service.file;

import static java.lang.Boolean.TRUE;

import com.sprint.discodeit.domain.entity.ReadStatus;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.domain.entity.UserStatus;
import com.sprint.discodeit.repository.file.BaseUserStatusRepository;
import com.sprint.discodeit.repository.file.FileUserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusService {

    private final FileUserRepository fileUserRepository;
    private final BaseUserStatusRepository baseUserStatusRepository;

    public ReadStatus dispatchChannelCreation(String channelName, List<UUID> userUUid, UUID channelId) {
        for(UUID u : userUUid) {
            if ("PRIVATE".equalsIgnoreCase(channelName)) {
                return createPrivateChannel(u, channelId);
            }
        }
        return null;
    }

    public ReadStatus createPrivateChannel(UUID userUUid, UUID channelId) {
        User user = fileUserRepository.findById(userUUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));
        Optional<UserStatus> userStatusOpt = baseUserStatusRepository.findById(user.getId());
        boolean isActive = userStatusOpt
                .map(status -> "Active".equalsIgnoreCase(status.getStatusType()))
                .orElse(false);  // UserStatus가 없으면 기본값 false

        // ReadStatus 객체 생성
        return new ReadStatus(user.getId(), Instant.now(), isActive, channelId);
    }
}
