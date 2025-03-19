package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusFindDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateDto readStatusCreateDto) {
        List<User> userList = userRepository.load();
        List<Channel> channelList = channelRepository.load();
        List<ReadStatus> readStatusList = readStatusRepository.load();

        User matchingUser = userList.stream()
                .filter(m -> m.getId().equals(readStatusCreateDto.userId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Channel matchingChannel = channelList.stream()
                .filter(m->m.getId().equals(readStatusCreateDto.channelId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        Optional<ReadStatus> matchingReadStatus = readStatusList.stream()
                .filter(m->m.getUserId().equals(readStatusCreateDto.userId()) && m.getChannelId().equals(readStatusCreateDto.channelId()))
                .findAny();
        if(matchingReadStatus.isPresent()){
            throw new IllegalArgumentException("Read status already exists");
        }
        ReadStatus readStatus = new ReadStatus(matchingUser.getId(), matchingChannel.getId());
        readStatusRepository.save(readStatus);
        return readStatus;

    }


    @Override
    public ReadStatus find(ReadStatusFindDto readStatusFindDto) {
        return readStatusRepository.load().stream()
                .filter(r->r.getId().equals(readStatusFindDto.Id()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("readStatus not found"));
    }


    @Override
    public List<ReadStatus> findAllByUserId(ReadStatusFindDto readStatusFindDto) {
        return readStatusRepository.load().stream()
                .filter(m->m.getUserId().equals(readStatusFindDto.userId()))
                .toList();
    }


    @Override
    public ReadStatus update(ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatus matchingReadStatus = readStatusRepository.load().stream()
                .filter(m->m.getUserId().equals(readStatusUpdateDto.userId()) && m.getChannelId().equals(readStatusUpdateDto.channelId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("readStatus not found"));
        matchingReadStatus.readStatusUpdate();
        return readStatusRepository.save(matchingReadStatus);
    }


    @Override
    public void delete(ReadStatusDeleteDto readStatusDeleteDto) {
        ReadStatus matchingReadStatus = readStatusRepository.load().stream()
                .filter(m->m.getId().equals(readStatusDeleteDto.Id()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("readStatus not found"));
        readStatusRepository.remove(matchingReadStatus);
    }
}
