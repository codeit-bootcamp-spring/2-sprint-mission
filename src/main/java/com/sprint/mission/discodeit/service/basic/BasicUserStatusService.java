package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.SaveUserStatusParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusByUserIdParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusParamDto;
import com.sprint.mission.discodeit.entity.UserStatus;
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

    @Override
    public void save(SaveUserStatusParamDto saveUserStatusParamDto) {
        UserStatus userStatus = UserStatus.builder()
                .userUUID(saveUserStatusParamDto.userUUID())
                .build();
        userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findById(UUID userStatusUUID) {
        return userStatusRepository.findById(userStatusUUID)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 상태"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public void update(UpdateUserStatusParamDto updateUserStatusParamDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(updateUserStatusParamDto.userStatusUUID())
                        .orElseThrow(() -> new NoSuchElementException("사용자 상태가 존재하지 않습니다."));
        userStatus.updateLastLoginTime();
        userStatusRepository.save(userStatus);
    }

    @Override
    public void updateByUserId(UpdateUserStatusByUserIdParamDto updateUserStatusByUserIdParamDto) {
        userStatusRepository.findAll().stream()
                .filter(userStatus -> userStatus.getUserUUID().equals(updateUserStatusByUserIdParamDto.id()))
                .findAny()
                .ifPresent(userStatus -> {
                    userStatus.updateLastLoginTime();
                    userStatusRepository.save(userStatus);
                });
    }

    @Override
    public void delete(UUID userStatusUUID) {
        userStatusRepository.delete(userStatusUUID);
    }
}
