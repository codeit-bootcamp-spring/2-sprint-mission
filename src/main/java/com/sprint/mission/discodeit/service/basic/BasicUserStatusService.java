package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.SaveUserStatusParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusParamDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        userStatusRepository.update(updateUserStatusParamDto.userStatusUUID());
    }

    @Override
    public void updateByUserId(UUID userUUID) {
        userStatusRepository.findAll().stream()
                .filter(userStatus -> userStatus.getUserUUID().equals(userUUID))
                .findAny()
                .ifPresent(userStatus -> {
                    userStatusRepository.update(userStatus.getId());
                });
    }

    @Override
    public void delete(UUID userStatusUUID) {
        userStatusRepository.delete(userStatusUUID);
    }
}
