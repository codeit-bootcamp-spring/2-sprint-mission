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
    public UserStatus createUserStatus(UserStatusCreateDto createDto) {
        checkUserExists(createDto.userId());
        checkUserStatusExistsByUserId(createDto.userId());

        return userStatusRepository.save(createDto.convertCreateDtoToUserStatus());
    }

    @Override
    public UserStatusResponseDto findById(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + id));
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
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 ID의 UserStatus를 찾을 수 없습니다: " + userId));
        return UserStatusResponseDto.convertToResponseDto(userStatus);
    }

    @Override
    public void updateUserStatus(UserStatusUpdateDto updateDto) {
        checkUserExists(updateDto.userId());
        UserStatus userStatus = userStatusRepository.findById(updateDto.id())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + updateDto.id()));

        userStatus.update(updateDto.lastActiveAt());
        userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        checkUserExists(userId);
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 ID의 UserStatus를 찾을 수 없습니다: " + userId));

        userStatus.updateByUserId();
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void deleteUserStatus(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + id));

        checkUserExists(userStatus.getUserId());

        userStatusRepository.deleteById(id);
    }


    /*******************************
     * Validation check
     *******************************/
    private void checkUserStatusExistsByUserId(UUID userId) {
        if(findByUserId(userId) == null){
            throw new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId);
        }
    }

    private void checkUserExists(UUID userId) {
        if(userRepository.findById(userId).isEmpty()){
            throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. : " + userId);
        }
    }

}
