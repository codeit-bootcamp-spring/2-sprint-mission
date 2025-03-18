package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.User.UserCRUDDTO;
import com.sprint.mission.discodeit.Exception.NotFoundExceptions;
import com.sprint.mission.discodeit.Exception.EmptyUserListException;
import com.sprint.mission.discodeit.Repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserRepository implements UserRepository {
    private final FileRepositoryImpl<List<User>> fileRepository;
    private  List<User> userList = new ArrayList<>();

    private final Path path =  Paths.get(System.getProperty("user.dir"), "data", "UserList.ser");


    public FileUserRepository() {
        this.fileRepository = new FileRepositoryImpl<>(path);
        fileRepository.load();

    }

    @Override
    public void reset() {
        fileRepository.init();
        try {
            Files.deleteIfExists(path);
            userList = new ArrayList<>();

        } catch (IOException e) {
            System.out.println("리스트 초기화 실패");
        }
    }

    @Override
    public User save(User user) {

        userList.add(user);
        fileRepository.save(userList);
        return user;
    }

    @Override
    public User find(UUID userId) {
        User user = CommonUtils.findById(userList, userId, User::getId)
                .orElseThrow(() -> NotFoundExceptions.USER_NOT_FOUND);

        return user;
    }

    @Override
    public List<User> findUserList() {
        if (userList.isEmpty()) {
            throw new EmptyUserListException("유저 리스트가 비어있습니다.");
        }
        return userList;
    }


    @Override
    public User update(User user, UserCRUDDTO userUpdateDTO) {
        if (userUpdateDTO.userId() != null) {
            user.setId(userUpdateDTO.userId());
        }
        if (userUpdateDTO.userName() != null) {
            user.setName(userUpdateDTO.userName());
        }
        if (userUpdateDTO.email() != null) {
            user.setEmail(userUpdateDTO.email());
        }
        if (userUpdateDTO.profileId() != null) {
            user.setProfileId(userUpdateDTO.profileId());
        }
        fileRepository.save(userList);
        return user;
    }


    @Override
    public UUID remove(User user) {
        if (userList.isEmpty()) {
            throw new EmptyUserListException("유저 리스트가 비어있습니다.");
        }
        userList.remove(user);
        fileRepository.save(userList);
        return user.getId();
    }
}

