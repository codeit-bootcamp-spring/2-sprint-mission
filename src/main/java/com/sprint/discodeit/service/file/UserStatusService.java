package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.StatusType;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.domain.entity.UserStatus;
import com.sprint.discodeit.repository.UserRepository;
import com.sprint.discodeit.repository.UserStatusRepository;
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
public class UserStatusService {

    private final BaseUserStatusRepository baseuserStatusRepository;
    private final FileUserRepository fileuserRepository;

    public User creat(UUID userId, StatusType statusType){
        User user = fileuserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Optional.ofNullable(user.getUserStatusId())
                .ifPresent(status -> {
                    throw new IllegalArgumentException("이미 상태값이 존재하는 사용자입니다.");
                });
        UserStatus userStatus = new UserStatus(Instant.now(), statusType.toString());
        user.associateStatus(userStatus);

        fileuserRepository.save(user);
        baseuserStatusRepository.save(userStatus);

        return user;
    }


    public UserStatus find(UUID statusId){
        UserStatus userStatus = baseuserStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 상태 정보 입니다."));
        return userStatus;
    }

    public List<UserStatus> findAll(){
        List<UserStatus> userStatus = baseuserStatusRepository.findByAll();
        return userStatus;
    }

    public void update(UUID statusId, StatusType statusType){
        UserStatus userStatus = baseuserStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 상태 정보 입니다."));
        userStatus.updateStatus(Instant.now(), statusType.toString());
        baseuserStatusRepository.save(userStatus);
    }

    public void delete(UUID userId){
        UUID byUserIdAndStatusId = fileuserRepository.findByUserIdAndStatusId(userId);
        fileuserRepository.delete(userId);
        baseuserStatusRepository.delete(byUserIdAndStatusId);
    }
}
