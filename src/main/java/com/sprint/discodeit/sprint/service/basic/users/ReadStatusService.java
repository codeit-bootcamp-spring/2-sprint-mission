package com.sprint.discodeit.sprint.service.basic.users;



import com.sprint.discodeit.sprint.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
//
//    public ReadStatus create(ReadStatusRequestDto readStatusRequestDto){
//        readStatusRepository.findById(readStatusRequestDto.)
//        return readStatus;
//    }


//    public ReadStatus find(UUID id){
//        ReadStatus readStatus = readStatusRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("조회가 불가능 합니다."));
//        return readStatus;
//    }
//
//    public ReadStatus findAll(UUID usersId){
//        ReadStatus readStatus = readStatusRepository.findAllByusersId(usersId)
//                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다"));
//        return readStatus;
//
//
//    }
//
//    public ReadStatus update(ReadStatusRequestDto readStatusRequestDto){
//        ReadStatus readStatus = readStatusRepository.findAllByusersId(readStatusRequestDto.usersId())
//                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다"));
//        readStatus.readUpdate(readStatusRequestDto.channelId(), readStatusRequestDto.check(), readStatusRequestDto.usersId());
//        return readStatus;
//    }
//
//    public void delete(UUID id){
//        readStatusRepository.delete(id);
//    }
//
//
//
//
//    public List<ReadStatus> createReadStatusesForPrivateChannel(List<UUID> usersIds, UUID channelId) {
//        List<ReadStatus> readStatuses = new ArrayList<>();
//
//        for (UUID usersId : usersIds) {
//            readStatuses.add(createPrivateChannel(usersId, channelId));
//        }
//
//        return readStatuses;
//    }
//
//    public ReadStatus createPrivateChannel(UUID usersUUid, UUID channelId) {
//        Users users = fileusersRepository.findById(usersUUid)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));
//        Optional<usersStatus> usersStatusOpt = baseusersStatusRepository.findById(users.getId());
//        boolean isActive = usersStatusOpt
//                .map(status -> "Active".equalsIgnoreCase(status.getStatusType()))
//                .orElse(false);  // usersStatus가 없으면 기본값 false
//
//        // ReadStatus 객체 생성
//        return new ReadStatus(users.getId(), Instant.now(), isActive, channelId);
//    }
//
//    public List<UUID> getusersIdsByChannel(UUID channelId) {
//        return readStatusRepository.findByusersIdAndChannelId(channelId);
//    }
}
