package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.SaveReadStatusParamDto;
import com.sprint.mission.discodeit.dto.UpdateReadStatusParamDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public void save(SaveReadStatusParamDto saveReadStatusParamDto) {
        User user = userRepository.findUserById(saveReadStatusParamDto.userUUID())
                        .orElseThrow(NullPointerException::new);

        Channel channel = channelRepository.findChannelById(saveReadStatusParamDto.channelUUID())
                        .orElseThrow(NullPointerException::new);

        ReadStatus readStatus = ReadStatus.builder()
                .channelId(channel.getId())
                .userId(user.getId())
                .build();

        readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus findById(UUID readStatusUUID) {
        return readStatusRepository.find(readStatusUUID)
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userUUID) {
        return readStatusRepository.findByUserId(userUUID);
    }

    @Override
    public void update(UpdateReadStatusParamDto updateReadStatusParamDto) {
        readStatusRepository.update(updateReadStatusParamDto.readStatusUUID());
    }

    @Override
    public void delete(UUID readStatusUUID) {
        readStatusRepository.delete(readStatusUUID);
    }
}
