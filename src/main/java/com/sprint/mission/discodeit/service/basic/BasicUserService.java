package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.DTO.User.UserFindDTO;
import com.sprint.mission.discodeit.Exception.CommonException;
import com.sprint.mission.discodeit.Exception.EmptyUserListException;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    private void checkDuplicate(UserCRUDDTO userCRUDDTO) {
        try {
            List<User> list = userRepository.findUserList();
            CommonUtils.checkUserDuplicate(list, userCRUDDTO.userName(), User::getName);
            CommonUtils.checkUserDuplicate(list, userCRUDDTO.email(), User::getEmail);
        } catch (EmptyUserListException e) {
        }
    }

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            userRepository.reset();
        }
    }

    @Override
    public User register(UserCRUDDTO userCRUDDTO) {
        UserCRUDDTO checkDuplicate = UserCRUDDTO.checkDuplicate(userCRUDDTO.userName(), userCRUDDTO.email());

        try {
            checkDuplicate(checkDuplicate);
        } catch (CommonException e) {
            System.out.println("중복된 유저가 존재합니다.");
            return null;
        }

        User user = new User(userCRUDDTO.userName(), userCRUDDTO.email(), userCRUDDTO.password());

        if (userCRUDDTO.binaryContent() != null) {
            user.setProfileId(userCRUDDTO.binaryContent().getBinaryContentId());
            try {
                BinaryContent content = binaryContentRepository.find(userCRUDDTO.binaryContent().getBinaryContentId());
            } catch (CommonException e) {
                BinaryContentDTO binaryContentDTO = BinaryContentDTO.create(userCRUDDTO.binaryContent());
                binaryContentRepository.save(binaryContentDTO);
            }
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userRepository.save(user);
        userStatusRepository.save(userStatus);
        return user;
    }


    @Override
    public UserFindDTO find(UUID userId) {
        User user = userRepository.find(userId);
        UserStatus userStatus = userStatusRepository.find(userId);

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
    public boolean delete(UserCRUDDTO userCRUDDTO) {
        try {
            User findUser = userRepository.find(userCRUDDTO.userId());

            userRepository.remove(findUser);
            userStatusRepository.delete(findUser.getId());
            binaryContentRepository.delete(findUser.profileId);

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
    public User update(UUID userId, UserCRUDDTO userCRUDDTO) {
        try {
            User findUser = userRepository.find(userId);
            User update = userRepository.update(findUser, userCRUDDTO);
            return update;
        } catch (IllegalArgumentException e0) {
            System.out.println("잘못된 ID값을 받았습니다.");
            return null;
        } catch (CommonException e1) {
            System.out.println("업데이트할 유저가 존재하지 않습니다.");
            return null;
        }
    }

}
