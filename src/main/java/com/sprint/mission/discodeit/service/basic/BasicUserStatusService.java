package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.service.dto.UserStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UUID createUserStatus(UserStatusCreateDto createDto) {
        checkUserExists(createDto.userId());

        return userStatusRepository.createUserStatus(createDto.convertCreateDtoToUserStatus());
    }

    @Override
    public UserStatusResponseDto findById(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id);
        return UserStatusResponseDto.convertToResponseDto(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> findAll() {
        List<UserStatus> userStatusList = userStatusRepository.findAll();
        return userStatusList.stream()
                .map(UserStatusResponseDto::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserStatusResponseDto findByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        return UserStatusResponseDto.convertToResponseDto(userStatus);
    }

    @Override
    public void updateUserStatus(UserStatusUpdateDto updateDto) {
        checkUserExists(updateDto.userId());

        userStatusRepository.updateUserStatus(updateDto.id(), updateDto.userId(), updateDto.lastActiveAt());
    }

    @Override
    public void updateByUserId(UUID userId) {
        checkUserExists(userId);

        userStatusRepository.updateByUserId(userId, Instant.now());
    }

    @Override
    public void deleteUserStatus(UUID id) {
        User user = userRepository.findById(id);
        checkUserExists(user.getId());

        userStatusRepository.deleteUserStatus(id);
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkUserExists(UUID userId) {
        if(userRepository.findById(userId) == null){
            throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. : " + userId);
        }
    }

}
