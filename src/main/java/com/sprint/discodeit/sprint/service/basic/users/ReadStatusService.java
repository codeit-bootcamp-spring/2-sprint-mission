package com.sprint.discodeit.sprint.service.basic.userss;

import com.sprint.discodeit.sprint.domain.dto.readStatusDto.ReadStatusRequestDto;
import com.sprint.discodeit.sprint.domain.entity.ReadStatus;
import com.sprint.discodeit.sprint.domain.entity.users;
import com.sprint.discodeit.sprint.domain.entity.usersStatus;
import com.sprint.discodeit.sprint.repository.file.BaseUsersStatusRepository;
import com.sprint.discodeit.sprint.repository.file.FileUsersRepository;
import com.sprint.discodeit.sprint.repository.file.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusService {

    private final FileUsersRepository fileusersRepository;
    private final BaseUsersStatusRepository baseusersStatusRepository;
    private final ReadStatusRepository readStatusRepository;

    public ReadStatus create(ReadStatusRequestDto readStatusRequestDto){
        ReadStatus readStatus = new ReadStatus(readStatusRequestDto.usersId(),Instant.now(), readStatusRequestDto.check(), readStatusRequestDto.usersId());
        return readStatus;
    }


    public ReadStatus find(UUID id){
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("조회가 불가능 합니다."));
        return readStatus;
    }

    public ReadStatus findAll(UUID usersId){
        ReadStatus readStatus = readStatusRepository.findAllByusersId(usersId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다"));
        return readStatus;


    }

    public ReadStatus update(ReadStatusRequestDto readStatusRequestDto){
        ReadStatus readStatus = readStatusRepository.findAllByusersId(readStatusRequestDto.usersId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다"));
        readStatus.readUpdate(readStatusRequestDto.channelId(), readStatusRequestDto.check(), readStatusRequestDto.usersId());
        return readStatus;
    }

    public void delete(UUID id){
        readStatusRepository.delete(id);
    }




    public List<ReadStatus> createReadStatusesForPrivateChannel(List<UUID> usersIds, UUID channelId) {
        List<ReadStatus> readStatuses = new ArrayList<>();

        for (UUID usersId : usersIds) {
            readStatuses.add(createPrivateChannel(usersId, channelId));
        }

        return readStatuses;
    }

    public ReadStatus createPrivateChannel(UUID usersUUid, UUID channelId) {
        users users = fileusersRepository.findById(usersUUid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));
        Optional<usersStatus> usersStatusOpt = baseusersStatusRepository.findById(users.getId());
        boolean isActive = usersStatusOpt
                .map(status -> "Active".equalsIgnoreCase(status.getStatusType()))
                .orElse(false);  // usersStatus가 없으면 기본값 false

        // ReadStatus 객체 생성
        return new ReadStatus(users.getId(), Instant.now(), isActive, channelId);
    }

    public List<UUID> getusersIdsByChannel(UUID channelId) {
        return readStatusRepository.findByusersIdAndChannelId(channelId);
    }
}
