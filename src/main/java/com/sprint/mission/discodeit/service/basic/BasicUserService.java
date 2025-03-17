package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.User.*;
import com.sprint.mission.discodeit.Exception.CommonException;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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

    private void checkDuplicate(UserCRUDDTO userCRUDDTO) {
        List<User> list = userRepository.findUserList();
        Optional<User> duplicateUser = list.stream()
                .filter(u -> u.getName().equals(userCRUDDTO.userName()) || u.getEmail().equals(userCRUDDTO.email())).findFirst();
        duplicateUser.ifPresent(u -> {
            throw CommonExceptions.DUPLICATE_USER;
        });
    }

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            userRepository.reset();
        }
    }

    @Override
    public UUID register(UserDTO userDTO) {
        UserCRUDDTO userCRUDDTO = UserCRUDDTO.create(userDTO.userName(), userDTO.email(), userDTO.email());

        UserCRUDDTO checkDuplicate = UserCRUDDTO.checkDuplicate(userCRUDDTO.userName(), userCRUDDTO.email());
        checkDuplicate(checkDuplicate);

        User user = new User(userCRUDDTO.userName(), userCRUDDTO.email(), userCRUDDTO.password());

        if (userCRUDDTO.binaryContent() != null) {
            user.setProfileId(userCRUDDTO.binaryContent().getBinaryContentId());

            BinaryContentCreateDTO binaryContentCreateDTO = new BinaryContentCreateDTO(userCRUDDTO.binaryContent());

            binaryContentRepository.save(binaryContentCreateDTO);
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userRepository.save(user);
        userStatusRepository.save(userStatus);
        return user.getId();
    }


    @Override
    public UserFindDTO find(String userId) {
        UUID userUUID = UUID.fromString(userId);
        User user = userRepository.find(userUUID);
        UserStatus userStatus = userStatusRepository.find(userUUID);

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


    @Override
    public boolean delete(UserDTO userDTO) {
        UserCRUDDTO userCRUDDTO = UserCRUDDTO.delete(userDTO.userId());
        try {
            User findUser = userRepository.find(userCRUDDTO.userId());

            userRepository.remove(findUser);
            userStatusRepository.delete(findUser.getId());
            binaryContentRepository.delete(findUser.getId());

            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (CommonException e) {
            System.out.println("유저를 찾지 못했습니다.");
            return false;
        }
    }


    @Override
    public boolean update(String userId, UserDTO userDTO) {
        UserCRUDDTO userCRUDDTO = UserCRUDDTO.update(userDTO.userId(),userDTO.profileId(),userDTO.userName(),userDTO.email());
        try {
            UUID userUUID = UUID.fromString(userId);
            User findUser = userRepository.find(userUUID);
            userRepository.update(findUser, userCRUDDTO);
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (CommonException e1) {
            System.out.println("업데이트할 유저가 존재하지 않습니다.");
            return false;
        }
    }

}
