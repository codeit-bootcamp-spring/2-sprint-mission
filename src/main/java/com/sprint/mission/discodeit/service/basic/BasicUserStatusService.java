package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;

    @Override
    public void create(UserStatusDto userStatusDto) {
        userStatusRepository.save(new UserStatus(userStatusDto.id(), userStatusDto.userId(), Boolean.FALSE));
    }

    @Override
    public UserStatusDto find(UUID id) {
        return userStatusRepository
                .findById(id)
                .map(UserStatusDto::from)
                .orElseThrow(() -> new IllegalArgumentException("상태 값을 찾지 못했습니다."));

    }

    @Override
    public List<UserStatusDto> findAll() {
            return userStatusRepository
                    .findAll()
                    .orElseThrow(() -> new IllegalArgumentException("상태 값을 찾지 못했습니다."))
                    .stream()
                    .map(UserStatusDto::from)
                    .collect(Collectors.toList());
    }

    @Override
    public void update(UserStatusDto userStatusDto){
        UserStatusDto userStatus = this.find(userStatusDto.userId());
        userStatusRepository.update(new UserStatus(userStatus.id(), userStatus.userId(), !userStatus.onLineStatus()));
    }

    @Override
    public void updateByUserId(UUID userid){
        UserStatusDto userStatus = this.find(userid);
        userStatusRepository.update(new UserStatus(userStatus.id(), userStatus.userId(), !userStatus.onLineStatus()));
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.delete(id);
    }
}
