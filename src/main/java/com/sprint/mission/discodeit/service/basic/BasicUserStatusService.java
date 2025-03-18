package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.UserStatus.CreatedUserStatusParam;
import com.sprint.mission.discodeit.dto.service.UserStatus.UpdateUserStatusParam;
import com.sprint.mission.discodeit.dto.service.UserStatus.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
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
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatusDTO create(CreatedUserStatusParam createdUserStatusParam) {
        checkUserExists(createdUserStatusParam);
        checkDuplicateUser(createdUserStatusParam);
        UserStatus userStatus = createUserStatusEntity(createdUserStatusParam);
        userStatusRepository.save(userStatus);
        return userStatusEntityToDTO(userStatus);
    }

    @Override
    public UserStatusDTO findById(UUID id) {
        UserStatus userStatus = findUserStatusById(id);
        return userStatusEntityToDTO(userStatus);
    }

    @Override
    public List<UserStatusDTO> findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        return userStatuses.stream()
                .map(us -> userStatusEntityToDTO(us))
                .toList();
    }

    @Override
    public UUID update(UpdateUserStatusParam updateUserStatusParam) {
        UserStatus userStatus = findUserStatusById(updateUserStatusParam.id());
        userStatus.updateUserStatus();
        userStatusRepository.save(userStatus);
        return userStatus.getId();
    }

    @Override
    public UUID updateByUserId(UUID userId, UpdateUserStatusParam updateUserStatusParam) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        userStatus.updateUserStatus();
        return userStatus.getId();
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.findById(id);
        userStatusRepository.deleteById(id);
    }

    private void checkUserExists(CreatedUserStatusParam createdUserStatusParam) {
        userRepository.findById(createdUserStatusParam.userId())
                .orElseThrow(() -> new NoSuchElementException(createdUserStatusParam.userId() + "유저가 존재하지 않습니다."));
    }

    private void checkDuplicateUser(CreatedUserStatusParam createdUserStatusParam) {
        if (userStatusRepository.existsByUserId(createdUserStatusParam.userId())) {
            throw new IllegalStateException(createdUserStatusParam.userId() + "의" + " UserStatus는 이미 존재합니다.");
        }
    }

    private UserStatus createUserStatusEntity(CreatedUserStatusParam createdUserStatusParam) {
        return UserStatus.builder()
                .userId(createdUserStatusParam.userId())
                .build();
    }

    private UserStatusDTO userStatusEntityToDTO(UserStatus userStatus) {
        return UserStatusDTO.builder()
                .cratedAt(userStatus.getCreatedAt())
                .id(userStatus.getUserId())
                .updatedAt(userStatus.getUpdatedAt())
                .userId(userStatus.getUserId())
                .build();
    }

    private UserStatus findUserStatusById(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + "UserStatus가 존재하지 않습니다."));
    }
}
