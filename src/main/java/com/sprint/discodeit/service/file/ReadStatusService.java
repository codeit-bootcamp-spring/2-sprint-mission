package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.entity.ReadStatus;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.repository.file.FileUserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusService {

    private final FileUserRepository fileUserRepository;

    public ReadStatus dispatchChannelCreation(String channelName, List<UUID> userUUid, Boolean check, UUID channelId) {
        for(UUID u : userUUid) {
            if ("PRIVATE".equalsIgnoreCase(channelName)) {
                return createPrivateChannel(u, check, channelId);
            }
        }
        return null;
    }

    public ReadStatus createPrivateChannel(UUID userUUid, Boolean check, UUID channelId) {
        User user = fileUserRepository.findById(userUUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));
        ReadStatus readStatus = new ReadStatus(user.getId(), Instant.now(), check, channelId);
        return readStatus;
    }
}
