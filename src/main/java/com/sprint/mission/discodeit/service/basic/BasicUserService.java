package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.BinaryContentDTO;
import com.sprint.mission.discodeit.DTO.UserService.UserCreateDTO;
import com.sprint.mission.discodeit.DTO.UserService.UserFindDTO;
import com.sprint.mission.discodeit.DTO.UserService.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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


    @Override
    public User create(UserCreateDTO userCreateDTO, BinaryContentDTO binaryContentDTO) {
        for (User user1 : userRepository.findAll()) {
            if (user1.getEmail().equals(userCreateDTO.email()) ||
                    user1.getUserName().equals(userCreateDTO.userName())) {
                System.out.println("이메일이나 닉네임이 이미 존재합니다.");
                return null;
            }
        }
        User user = userCreateDTO.toEntity();
        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);
        if(binaryContentDTO != null){
            BinaryContent binaryContent = binaryContentDTO.toEntity(binaryContentDTO);
            user.useProfileId(binaryContent.getId());
            binaryContentRepository.save(binaryContent);
        }
        return userRepository.save(user);
    }

    @Override
    public User find(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public UserFindDTO findWithStatus(UUID id) {
        User user = userRepository.findById(id);
        UserStatus userStatus = userStatusRepository.findById(id);
        return new UserFindDTO(user, userStatus);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserFindDTO> findAllWithStatus() {
        List<User> userList = userRepository.findAll();
        List<UserFindDTO> userFindDTOList = new ArrayList<>();

        userList.forEach(user -> {
           userFindDTOList.add(findWithStatus(user.getId()));
        });

        return userFindDTOList;
    }

    @Override
    public User update(UserUpdateDTO userUpdateDTO) {
        if(userUpdateDTO.photo() != null){
            binaryContentRepository.delete(userRepository.findById(userUpdateDTO.id()).getProfileId());
            BinaryContent binaryContent = new BinaryContent(BinaryContentType.IMAGE, userUpdateDTO.photo());
            binaryContentRepository.save(binaryContent);
            userRepository.findById(userUpdateDTO.id()).useProfileId(binaryContent.getId());
        }
        for (User user1 : userRepository.findAll()) {
            if (user1.getEmail().equals(userUpdateDTO.email()) ||
                    user1.getUserName().equals(userUpdateDTO.userName())) {
                System.out.println("이메일이나 닉네임이 이미 존재합니다.");
                return null;
            }
        }
        return userRepository.update(userUpdateDTO.id(),userUpdateDTO.userName(),userUpdateDTO.email(),userUpdateDTO.password());
    }

    @Override
    public void delete(UUID userId) {
        if(userRepository.findById(userId).getProfileId() !=null) {
            binaryContentRepository.delete(userRepository.findById(userId).getProfileId());
        }
        userStatusRepository.findById(userId);
        userRepository.delete(userId);

    }


}
