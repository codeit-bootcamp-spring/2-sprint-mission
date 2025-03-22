package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserFindDTO;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.request.CreateUserRequestDTO;
import com.sprint.mission.discodeit.dto.request.UpdateUserRequestDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.Valid.DuplicateUserException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            userRepository.reset();
        }
    }

    @CustomLogging
    @Override
    public UUID create(CreateUserRequestDTO userCreateDTO, Optional<CreateBinaryContentRequestDTO> binaryContentDTO) {

        checkDuplicate(userCreateDTO.name(), userCreateDTO.email());

        User user = new User(userCreateDTO.name(), userCreateDTO.email(), userCreateDTO.password());
        UUID profileId = makeBinaryContent(binaryContentDTO);
        user.setProfileId(profileId);
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return user.getId();
    }

    @Override
    public UserFindDTO findById(UUID userId) {
        User user = userRepository.findById(userId);

        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        userStatus.updateStatus();

        UserFindDTO userFindDTO = new UserFindDTO(
                user.getId(),
                user.getProfileId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
//                userStatus
        );

        return userFindDTO;
    }

    @Override
    public List<UserFindDTO> listAllUsers() {
        List<User> userList = userRepository.findAll();


        return userList.stream().map(user -> new UserFindDTO(
                user.getId(),
                user.getProfileId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt())).toList();
    }


    @CustomLogging
    @Override
    public UUID update(UUID userId, UpdateUserRequestDTO updateUserRequestDTO, Optional<CreateBinaryContentRequestDTO> binaryContentDTO) {
        User user = userRepository.findById(userId);
        UUID profileId = user.getProfileId();
        if (profileId != null) {
            binaryContentRepository.delete(profileId);
        }
        UUID newProfileId = makeBinaryContent(binaryContentDTO);

        User update = userRepository.update(user, updateUserRequestDTO, newProfileId);

        return update.getId();
    }

    @CustomLogging
    @Override
    public void delete(UUID userId) {
        User findUser = userRepository.findById(userId);
        userRepository.remove(findUser);
        userStatusRepository.delete(findUser.getId());

        Optional.ofNullable(findUser.getProfileId())
                .ifPresent(binaryContentRepository::delete);

    }

    private UUID makeBinaryContent(Optional<CreateBinaryContentRequestDTO> binaryContentDTO) {

        return binaryContentDTO.map(contentDTO -> {
            String fileName = contentDTO.fileName();
            String contentType = contentDTO.contentType();
            byte[] bytes = contentDTO.bytes();
            long size = (long) bytes.length;

            BinaryContent content = new BinaryContent(fileName, size, contentType, bytes);
            binaryContentRepository.save(content);
            return content.getId();
        }).orElse(null);
    }

    private void checkDuplicate(String name, String email) {
        if (userRepository.existName(name) || userRepository.existEmail(email)) {
            throw new DuplicateUserException("동일한 유저가 존재합니다.");
        }
    }
}
