package com.sprint.discodeit.sprint5.service.basic.users;

import com.sprint.discodeit.sprint5.domain.StatusType;
import com.sprint.discodeit.sprint5.domain.entity.users;
import com.sprint.discodeit.sprint5.domain.entity.usersStatus;
import com.sprint.discodeit.sprint5.repository.file.BaseUsersStatusRepository;
import com.sprint.discodeit.sprint5.repository.file.FileUsersRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersStatusService {

    private final BaseUsersStatusRepository baseusersStatusRepository;
    private final FileUsersRepository fileusersRepository;

    public users creat(UUID usersId, StatusType statusType){
        users users = fileusersRepository.findById(usersId)
                .orElseThrow(() -> new IllegalArgumentException("users not found"));
        Optional.ofNullable(users.getUsersStatusId())
                .ifPresent(status -> {
                    throw new IllegalArgumentException("이미 상태값이 존재하는 사용자입니다.");
                });
        usersStatus usersStatus = new usersStatus(Instant.now(), statusType.toString());
        users.associateStatus(usersStatus);

        fileusersRepository.save(users);
        baseusersStatusRepository.save(usersStatus);

        return users;
    }


    public usersStatus find(UUID statusId){
        usersStatus usersStatus = baseusersStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 상태 정보 입니다."));
        return usersStatus;
    }

    public List<usersStatus> findAll(){
        List<usersStatus> usersStatus = baseusersStatusRepository.findByAll();
        return usersStatus;
    }

    public void update(UUID statusId, StatusType statusType){
        usersStatus usersStatus = baseusersStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 상태 정보 입니다."));
        usersStatus.updateStatus(Instant.now(), statusType.toString());
        baseusersStatusRepository.save(usersStatus);
    }

    public void delete(UUID usersId){
        UUID byusersIdAndStatusId = fileusersRepository.findByusersIdAndStatusId(usersId);
        fileusersRepository.delete(usersId);
        baseusersStatusRepository.delete(byusersIdAndStatusId);
    }
}
