package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatusService.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatusService.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest request) {
        if(userRepository.findById(request.userId())==null){
            throw new NoSuchElementException("유저를 찾을 수 없습니다.");
        }
        if(userStatusRepository.findByUserId(request.userId())!=null){
            throw new IllegalArgumentException("이미 존재하는 userStatus입니다.");
        }

        UserStatus userStatus = new UserStatus(request.userId());

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findById(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findById(request.id());
        if(userStatus==null){
            throw new NoSuchElementException("userStatus " + request.id() + " 를 찾을 수 없습니다.");
        }

        userStatus.updateLastLogin();

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID id) {
        UserStatus userStatus = userStatusRepository.findByUserId(id);
        if(userStatus==null){
            throw new NoSuchElementException("userStatus " + id + " 를 찾을 수 없습니다.");
        }
        userStatus.updateLastLogin();

        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.delete(id);
    }
}
