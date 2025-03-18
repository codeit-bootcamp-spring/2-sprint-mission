package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.DTO.User.UserFindDTO;
import com.sprint.mission.discodeit.Exception.Empty.EmptyUserListException;
import com.sprint.mission.discodeit.Exception.NotFound.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.Exception.NotFound.UserNotFoundException;
import com.sprint.mission.discodeit.Exception.NotFound.UserStatusNotFoundException;
import com.sprint.mission.discodeit.Exception.Valid.DuplicateUserException;
import com.sprint.mission.discodeit.Exception.legacy.EmptyException;
import com.sprint.mission.discodeit.Exception.legacy.NotFoundException;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.logging.CustomLogging;
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
    public User register(UserCRUDDTO userCRUDDTO, Optional<BinaryContentDTO> binaryContentDTO) {
        UserCRUDDTO checkDuplicate = UserCRUDDTO.checkDuplicate(userCRUDDTO.userName(), userCRUDDTO.email());

        try {
            checkDuplicate(checkDuplicate);
            User user = new User(userCRUDDTO.userName(), userCRUDDTO.email(), userCRUDDTO.password());
            UUID profileId = makeBinaryContent(binaryContentDTO);
            user.setProfileId(profileId);
            userRepository.save(user);

            UserStatus userStatus = new UserStatus(user.getId());
            userStatusRepository.save(userStatus);

            return user;
        } catch (DuplicateUserException e) {
            throw new DuplicateUserException("중복된 유저가 존재합니다.");
        }
    }

    @Override
    public UserFindDTO find(UUID userId) {
        User user = userRepository.find(userId);
        UserStatus userStatus = userStatusRepository.find(userId);
        userStatus.isOnline();

        UserFindDTO userFindDTO = UserFindDTO.find(
                user.getId(),
                user.getProfileId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                userStatus
        );

        return userFindDTO;
    }

    @Override
    public List<UserFindDTO> findAll() {
        List<UserFindDTO> findList = new ArrayList<>();

        List<User> userList = userRepository.findUserList();
        for (User user : userList) {
            UserStatus userStatus = userStatusRepository.find(user.getId());
            UserFindDTO userFindDTO = UserFindDTO.find(
                    user.getId(),
                    user.getProfileId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCreatedAt(),
                    user.getUpdatedAt(),
                    userStatus
            );
            findList.add(userFindDTO);
        }
        return findList;
    }


    @Override
    public void print() {
        List<User> list = userRepository.findUserList();
        for (User user : list) {
            System.out.println(user);
        }
    }

    @CustomLogging
    @Override
    public User update(UUID userId, UserCRUDDTO userCRUDDTO) {
        try {
            User findUser = userRepository.find(userId);
            User update = userRepository.update(findUser, userCRUDDTO);
            return update;
        } catch (EmptyException e) {
            System.out.println("업데이트할 리스트가 존재하지 않습니다.");
            return null;
        } catch (NotFoundException e) {
            System.out.println("업데이트할 유저가 존재하지 않습니다.");
            return null;
        }
    }

    @CustomLogging
    @Override
    public boolean delete(UserCRUDDTO userCRUDDTO) {
        try {
            User findUser = userRepository.find(userCRUDDTO.userId());

            userRepository.remove(findUser);
            userStatusRepository.delete(findUser.getId());

            UUID profileId = findUser.profileId;

            if (profileId != null) {
                binaryContentRepository.delete(profileId);
            }

            return true;
        } catch (UserNotFoundException e) {
            System.out.println("유저를 찾지 못했습니다.");
            return false;
        } catch (UserStatusNotFoundException e) {
            System.out.println("유저 상태를 찾지 못했습니다.");
            return false;
        } catch (BinaryContentNotFoundException e) {
            System.out.println("바이너리 정보를 찾지 못했습니다.");
            return false;
        } catch (EmptyUserListException e) {
            System.out.println("유저 리스트가 비어있습니다.");
            return false;
        }
    }

    private UUID makeBinaryContent(Optional<BinaryContentDTO> binaryContentDTO) {
        UUID profileId = binaryContentDTO.map(contentDTO -> {
            String fileName = contentDTO.fileName();
            String contentType = contentDTO.contentType();
            byte[] bytes = contentDTO.bytes();
            int size = bytes.length;

            BinaryContent content = new BinaryContent(fileName, size, contentType, bytes);
            binaryContentRepository.save(content);
            return content.getBinaryContentId();
        }).orElse(null);

        return profileId;
    }

    private void checkDuplicate(UserCRUDDTO userCRUDDTO) {
        try {
            List<User> list = userRepository.findUserList();
            CommonUtils.checkUserDuplicate(list, userCRUDDTO.userName(), User::getName);
            CommonUtils.checkUserDuplicate(list, userCRUDDTO.email(), User::getEmail);
        } catch (EmptyUserListException e) {
        }
    }
}
