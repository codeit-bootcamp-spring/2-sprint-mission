package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusInfoDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatusServiceImpl(UserStatusRepository userStatusRepository, UserRepository userRepository) {
        this.userStatusRepository = userStatusRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserStatusInfoDto findById(UUID id) {
        UserStatus status = userStatusRepository.findUserStatusById(id);
        if (status == null) throw new IllegalArgumentException("UserStatus를 찾을 수 없습니다.");
        return convert(status);
    }

    @Override
    public List<UserStatusInfoDto> findAll() {
        return userStatusRepository.findAllUserStatus()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void create(CreateUserStatusDto dto) {
        if (userRepository.findUserById(dto.getUserid()) == null) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        if (userStatusRepository.findUserStatusById(dto.getUserid()) != null) {
            throw new IllegalStateException("이미 상태가 존재합니다.");
        }

        UserStatus status = new UserStatus(dto.getUserid());
        userStatusRepository.addUserStatus(status);
    }

    @Override
    public void update(UpdateUserStatusDto dto) {
        UserStatus status = userStatusRepository.findUserStatusById(dto.getId());
        if (status == null) throw new IllegalArgumentException("UserStatus를 찾을 수 없습니다.");
        status.updateLastActiveAt();
        userStatusRepository.addUserStatus(status);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteUserStatusById(id);
    }

    private UserStatusInfoDto convert(UserStatus status) {
        return new UserStatusInfoDto(status.getUserid(), status.getUserid(), status.isUserOnline(), status.getUpdatedAt());
    }
}
