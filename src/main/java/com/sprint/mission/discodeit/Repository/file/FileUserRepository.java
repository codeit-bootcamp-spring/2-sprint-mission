package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.User.UserUpdateDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public UUID save(User user) {

        userList.add(user);
        fileRepository.save(userList);
        return user.getId();
    }

    @Override
    public User find(UUID userId) {
        User user = CommonUtils.filterById(userList, userId, User::getId)
                .findFirst()
                .orElseThrow(() -> CommonExceptions.USER_NOT_FOUND);

        return user;
    }

    @Override
    public List<User> findUserList() {
        if (userList.isEmpty()) {
            throw CommonExceptions.EMPTY_USER_LIST;
        }
        return userList;
    }


    @Override
    public UUID update(User user, UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.replaceId() != null) {
            user.setId(userUpdateDTO.replaceId());
        }
        if (userUpdateDTO.replaceName() != null) {
            user.setName(userUpdateDTO.replaceName());
        }
        if (userUpdateDTO.replaceEmail() != null) {
            user.setEmail(userUpdateDTO.replaceEmail());
        }
        if (userUpdateDTO.binaryContentId() != null) {
            user.setProfileId(userUpdateDTO.binaryContentId());
        }
        fileRepository.save(userList);
        return user.getId();
    }


    @Override
    public UUID remove(User user) {
        if (userList.isEmpty()) {
            throw CommonExceptions.EMPTY_USER_LIST;
        }
        userList.remove(user);
        fileRepository.save(userList);
        return user.getId();
    }
}

