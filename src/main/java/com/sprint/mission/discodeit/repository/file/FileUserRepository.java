package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.requestToService.UserUpdateDTO;
import com.sprint.mission.discodeit.exception.NotFound.SaveFileNotFoundException;
import com.sprint.mission.discodeit.exception.Empty.EmptyUserListException;
import com.sprint.mission.discodeit.exception.NotFound.UserNotFoundException;
import com.sprint.mission.discodeit.repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.CommonUtils;
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
        try {
            this.userList = fileRepository.load();
        } catch (SaveFileNotFoundException e) {
            System.out.println("FileUserRepository init");
        }
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
    public User findById(UUID userId) {
        User user = CommonUtils.findById(userList, userId, User::getId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        return user;
    }

    @Override
    public List<User> findAll() {
        if (userList.isEmpty()) {
            throw new EmptyUserListException("유저 리스트가 비어있습니다.");
        }
        return userList;
    }


    @Override
    public User update(User user, UserUpdateDTO userUpdateDTO, UUID replaceProfileId) {
        if (userUpdateDTO.replaceName() != null) {
            user.setName(userUpdateDTO.replaceName());
        }
        if (userUpdateDTO.replaceEmail() != null) {
            user.setEmail(userUpdateDTO.replaceEmail());
        }
        if (replaceProfileId != null) {
            user.setProfileId(replaceProfileId);
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


    @Override
    public boolean existId(UUID id) {
        return userList.stream().anyMatch(u -> u.getId().equals(id));
    }

    @Override
    public boolean existName(String name) {
        return userList.stream().anyMatch(u -> u.getName().equals(name));
    }

    @Override
    public boolean existEmail(String email) {
        return userList.stream().anyMatch(u -> u.getEmail().equals(email));
    }

}

