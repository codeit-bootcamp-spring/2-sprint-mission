package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusService.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusService.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        User user = userRepository.findById(request.userId());
        if (user == null){
            throw new NoSuchElementException("유저가 존재하지 않습니다.");
        }

        Channel channel = channelRepository.findById(request.channelId());
        if (channel == null){
            throw new NoSuchElementException("채널이 존재하지 않습니다.");
        }
        if(readStatusRepository.findAllByUserId(user.getId()).stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(channel.getId()))){
            throw new IllegalArgumentException("이미 존재하는 readStatus입니다.");
        }

        ReadStatus readStatus = new ReadStatus(request.userId(),request.channelId());

        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readStatusRepository.find(id);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID id) {
        return readStatusRepository.findAllByUserId(id);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequest request) {
        ReadStatus readStatus = findById(request.readStatusId());
        if (readStatus == null){
            throw new NoSuchElementException("ReadStatus " +  request.readStatusId() + " 를 찾을 수 없습니다.");
        }
        readStatus.updateLastCheckedAt();
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }
}
