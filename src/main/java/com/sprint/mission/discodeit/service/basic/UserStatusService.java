package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatus create(UserStatusCreateRequest request) {
//        관련된 User가 존재하지 않으면 예외를 발생시킵니다.
        User user = userRepository.findById(request.userId())
                .orElseThrow(()-> new NoSuchElementException("User not found"));
//        같은 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
        if(userStatusRepository.findByUserId(request.userId()).isPresent()){
            throw new IllegalArgumentException("User with id " + request.userId() + " already exists");
        }

        UserStatus userStatus = new UserStatus(user.getId(), request.lastOnlineAt());
        userStatusRepository.save(userStatus);

        return userStatus;
    }

    public UserStatus find(UUID id){
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus does not exist"));
    }

    public List<UserStatus> findAll(){
        return userStatusRepository.findAll();
    }

    public UserStatus update(UUID userId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus does not exist"));

        userStatus.updateLastOnline(request.lastOnLineAt());

        return userStatusRepository.save(userStatus);
    }

    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        // userId 로 특정 User의 객체를 업데이트합니다.
        // 뭘 업데이트 하라는 걸까?
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus does not exist"));

        userStatus.updateLastOnline(Instant.now());

        return userStatusRepository.save(userStatus);
    }

    public void delete(UUID id){
        userStatusRepository.delete(id);
    }

}
