package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.DTO.User.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.User.UserDeleteDTO;
import com.sprint.mission.discodeit.DTO.User.UserFindDTO;
import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;
import com.sprint.mission.discodeit.Exception.DuplicateUserException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
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

    private void checkDuplicate(String userName, String email) {
        List<User> list = userRepository.findUserList();
        Optional<User> duplicateUser = list.stream()
                .filter(u -> u.getName().equals(userName) || u.getEmail().equals(email)).findFirst();
        duplicateUser.ifPresent(u -> {
            throw new DuplicateUserException("해당 이름, 이메일을 가진 유저가 존재합니다.");
        });
    }

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            userRepository.reset();
        }
    }

    @Override
    public UUID register(UserCreateDTO userCreateDTO) {
        checkDuplicate(userCreateDTO.userName(), userCreateDTO.email());
        User user = new User(userCreateDTO.userName(), userCreateDTO.email(), userCreateDTO.password());

        if (userCreateDTO.binaryContent() != null) {
            user.setProfileId(userCreateDTO.binaryContent().getBinaryContentId());
            BinaryContentCreateDTO binaryContentCreateDTO = new BinaryContentCreateDTO(userCreateDTO.binaryContent());
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

        UserFindDTO userFindDTO = new UserFindDTO(
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

    //이게 맞나?
    //일일이 넣는 것이?
    @Override
    public List<UserFindDTO> findAll() {
        List<UserFindDTO> findList = new ArrayList<>();

        List<User> userList = userRepository.findUserList();
        for (User user : userList) {
            UserStatus userStatus = userStatusRepository.find(user.getId());
            UserFindDTO userFindDTO = new UserFindDTO(
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
    public boolean delete(UserDeleteDTO userDeleteDTO) {
        try {
            UUID userUUID = UUID.fromString(userDeleteDTO.userId());
            User findUser = userRepository.find(userUUID);

            userRepository.remove(findUser);
            userStatusRepository.delete(findUser.getId());
            binaryContentRepository.delete(findUser.getId());

            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e) {
            System.out.println("유저를 찾지 못했습니다.");
            return false;
        }
    }


    @Override
    public boolean update(String userId, UserUpdateDTO userUpdateDTO) {
        try {
            UUID userUUID = UUID.fromString(userId);
            User findUser = userRepository.find(userUUID);
            userRepository.update(findUser, userUpdateDTO);
            return true;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return false;
        } catch (UserNotFoundException e1) {
            System.out.println("업데이트할 유저가 존재하지 않습니다.");
            return false;
        }
    }

}
