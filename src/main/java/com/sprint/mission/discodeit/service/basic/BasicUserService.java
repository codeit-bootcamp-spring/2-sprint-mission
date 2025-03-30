package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.UserService.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserService.UserFindDto;
import com.sprint.mission.discodeit.dto.UserService.UserUpdateRequest;
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
    public User create(UserCreateRequest userCreateRequest, BinaryContentDTO binaryContentDTO) {
        for (User user1 : userRepository.findAll()) {
            if (user1.getEmail().equals(userCreateRequest.email()) ||
                    user1.getUserName().equals(userCreateRequest.userName())) {
                System.out.println("이메일이나 닉네임이 이미 존재합니다.");
                return null;
            }
        }
        User user = userCreateRequest.toEntity();
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
    public UserFindDto findWithStatus(UUID id) {
        User user = userRepository.findById(id);
        UserStatus userStatus = userStatusRepository.findByUserId(id);
        return new UserFindDto(user, userStatus);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserFindDto> findAllWithStatus() {
        List<User> userList = userRepository.findAll();
        List<UserFindDto> userFindDtoList = new ArrayList<>();

        userList.forEach(user -> {
           userFindDtoList.add(findWithStatus(user.getId()));
        });

        return userFindDtoList;
    }

    @Override
    public User update( UUID id, UserUpdateRequest userUpdateRequest) {
        if(userUpdateRequest.photo() != null){
            binaryContentRepository.delete(userRepository.findById(id).getProfileId());
            BinaryContent binaryContent = new BinaryContent(BinaryContentType.IMAGE, userUpdateRequest.photo());
            binaryContentRepository.save(binaryContent);
            userRepository.findById(id).useProfileId(binaryContent.getId());
        }
        for (User user1 : userRepository.findAll()) {
            if (user1.getEmail().equals(userUpdateRequest.email()) ||
                    user1.getUserName().equals(userUpdateRequest.userName())) {
                System.out.println("이메일이나 닉네임이 이미 존재합니다.");
                return null;
            }
        }
        return userRepository.update(id, userUpdateRequest.userName(), userUpdateRequest.email(), userUpdateRequest.password());
    }

    @Override
    public void delete(UUID userId) {
        if(userRepository.findById(userId).getProfileId() !=null) {
            binaryContentRepository.delete(userRepository.findById(userId).getProfileId());
        }
        userStatusRepository.findByUserId(userId);
        userRepository.delete(userId);

    }



}
