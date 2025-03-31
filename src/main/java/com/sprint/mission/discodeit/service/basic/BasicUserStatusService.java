package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserStatusDTO;
import com.sprint.mission.discodeit.dto.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserStatusDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Getter
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(CreateUserStatusDTO createUserStatusDTO) {
        User user = userRepository.findById(createUserStatusDTO.getUserId())
                .orElseThrow(()-> new NoSuchElementException("User not found"));
        Optional<UserStatus> existingUserStatus = userStatusRepository.findByUserId(user.getId());
        if (existingUserStatus.isPresent()) {
            throw new IllegalStateException("User status already exists");
        }
        Instant lastModified = createUserStatusDTO.getLastModified() != null ? createUserStatusDTO.getLastModified() : Instant.now();

        UserStatus userStatus = new UserStatus(
                UUID.randomUUID(),
                user.getId(),
                lastModified
        );
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findByUserId(id)
                .orElseThrow(()-> new NoSuchElementException("User not found"));
    }

    @Override
    public List<UserStatus> findAll(){
        return userStatusRepository.findAll();
    }

    @Override
    public void update(UpdateUserStatusDTO userStatusDTO) {
        UserStatus userStatus = userStatusRepository.findByUserId(userStatusDTO.getId())
                .orElseThrow(()-> new NoSuchElementException("User not found"));
        userStatus.setLastLogin(userStatusDTO.getLastModified());
        userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, Instant lastModified) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(()-> new NoSuchElementException("User not found"));
        userStatus.setLastLogin(lastModified);
        userStatusRepository.save(userStatus);
        return userStatus;
    }
    @Override
    public void delete(UUID id){
        UserStatus userStatus = userStatusRepository.findByUserId(id)
                .orElseThrow(()-> new NoSuchElementException("User not found"));
        userStatusRepository.delete(userStatus);
    }
}
