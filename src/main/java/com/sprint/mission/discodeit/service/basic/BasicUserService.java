package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserFindDTO;
import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDTO;
import com.sprint.mission.discodeit.dto.request.CreateUserRequestDTO;
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

import java.util.ArrayList;
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

        List<UserFindDTO> list = userList.stream().map(user -> new UserFindDTO(
                user.getId(),
                user.getProfileId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt())).toList();

        return list;
    }
//
//    @CustomLogging
//    @Override
//    public User update(String userId, UserUpdateDTO userUpdateDTO, Optional<CreateBinaryContentRequestDTO>binaryContentDTO) {
//        UUID userUUID = UUID.fromString(userId);
//        try {
//            User findUser = userRepository.find(userUUID);
//            UUID newProfileId = makeBinaryContent(binaryContentDTO);
//
//            User update = userRepository.update(findUser, userUpdateDTO, newProfileId);
//
//            return update;
//        } catch (UserNotFoundException e) {
//            System.out.println("update: 해당 유저가 존재하지 않습니다.");
//            return null;
//        }
//    }
//
//    @CustomLogging
//    @Override
//    public boolean delete(String userId) {
//        UUID userUUID = UUID.fromString(userId);
//        try {
//            User findUser = userRepository.find(userUUID);
//
//            userRepository.remove(findUser);
//            userStatusRepository.delete(findUser.getId());
//
//            Optional.ofNullable(findUser.getProfileId())
//                    .ifPresent(binaryContentRepository::delete);
//
//            return true;
//        } catch (UserNotFoundException e) {
//            System.out.println("delete: 유저를 찾지 못했습니다.");
//            return false;
//        } catch (UserStatusNotFoundException e) {
//            System.out.println("delete: 유저 상태를 찾지 못했습니다.");
//            return false;
//        } catch (BinaryContentNotFoundException e) {
//            System.out.println("delete: 바이너리 정보를 찾지 못했습니다.");
//            return false;
//        } catch (EmptyUserListException e) {
//            System.out.println("delete: 유저 리스트가 비어있습니다.");
//            return false;
//        }
//    }

    private UUID makeBinaryContent(Optional<CreateBinaryContentRequestDTO> binaryContentDTO) {
        UUID profileId = binaryContentDTO.map(contentDTO -> {
            String fileName = contentDTO.fileName();
            String contentType = contentDTO.contentType();
            byte[] bytes = contentDTO.bytes();
            long size = (long) bytes.length;

            BinaryContent content = new BinaryContent(fileName, size, contentType, bytes);
            binaryContentRepository.save(content);
            return content.getId();
        }).orElse(null);

        return profileId;
    }

    private void checkDuplicate(String name, String email) {
        if (userRepository.existName(name) || userRepository.existEmail(email)) {
            throw new DuplicateUserException("동일한 유저가 존재합니다.");
        }
    }
}
