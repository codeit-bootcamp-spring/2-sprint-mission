package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateParam;
import com.sprint.mission.discodeit.service.dto.UserCreateParam;
import com.sprint.mission.discodeit.service.dto.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.UserStatusParam;
import com.sprint.mission.discodeit.service.dto.UserUpdateParam;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUserService implements UserService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    private final UserStatusService userStatusService;
    private final BasicBinaryContentService basicBinaryContentService;


    public FileUserService(UserStatusService userStatusService, BasicBinaryContentService basicBinaryContentService) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", User.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.userStatusService = userStatusService;
        this.basicBinaryContentService = basicBinaryContentService;
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    private boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(u -> u.getUsername().equals(username));
    }

    private boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    private void duplicateUsername(String username) {
        if (existsByUsername(username)) {
            throw new IllegalArgumentException(username + " 은 중복된 username.");
        }
    }

    private void duplicateEmail(String email) {
        if (existsByEmail(email)) {
            throw new IllegalArgumentException(email + " 은 중복된 email.");
        }
    }

    private UUID profileCreate(BinaryContentType type, List<MultipartFile> file) {
        BinaryContentCreateParam binaryContentCreateParam = new BinaryContentCreateParam(type, file);
        List<UUID> idList = basicBinaryContentService.create(binaryContentCreateParam);
        return idList.get(0);
    }

    @Override
    public User create(UserCreateParam createParam) {
        duplicateUsername(createParam.getUsername());
        duplicateEmail(createParam.getEmail());

        UUID binaryContentId = null;

        if (createParam.getType() != null && createParam.getFile() != null && !createParam.getFile().isEmpty()) {
            binaryContentId = profileCreate(createParam.getType(), List.of(createParam.getFile()));
        }

        User user = new User(createParam.getUsername(), createParam.getEmail(), createParam.getPassword(),
                binaryContentId);

        UserStatusParam statusParam = new UserStatusParam(user.getId(), UserStatusType.ONLINE);
        userStatusService.create(statusParam);

        Path path = resolvePath(user.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public UserInfoResponse find(UUID userId) {
        User userNullable = null;
        Path path = resolvePath(userId);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                userNullable = (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        UserStatus userStatus = userStatusService.findByUserId(user.getId());

        return new UserInfoResponse(user.getId(),
                user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
                user.getEmail(), user.getProfileId(), userStatus.getStatus());
    }

    @Override
    public List<UserInfoResponse> findAll() {
        try {
            List<User> userList = Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (User) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            return userList.stream()
                    .map(user -> {
                        UserStatus userStatus = userStatusService.findByUserId(user.getId());
                        return new UserInfoResponse(
                                user.getId(), user.getCreatedAt(),
                                user.getUpdatedAt(), user.getUsername(),
                                user.getEmail(), user.getProfileId(),
                                userStatus.getStatus()
                        );
                    }).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User update(UserUpdateParam updateParam) {
        duplicateUsername(updateParam.getNewUsername());
        duplicateEmail(updateParam.getNewEemail());

        User userNullable = null;

        Path path = resolvePath(updateParam.getId());
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                userNullable = (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + updateParam.getId() + " not found"));
        UUID binaryContentId = user.getProfileId();

        if (updateParam.getNewType() != null && updateParam.getNewFile() != null
                && !updateParam.getNewFile().isEmpty()) {
            if (binaryContentId != null) {
                basicBinaryContentService.delete(user.getProfileId());
            }
            binaryContentId = profileCreate(updateParam.getNewType()
                    , List.of(updateParam.getNewFile()));
        }

        user.update(updateParam.getNewUsername(), updateParam.getNewEemail()
                , updateParam.getNewPassword(), binaryContentId);
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public void delete(UUID userId) {
        Path path = resolvePath(userId);
        if (Files.notExists(path)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        basicBinaryContentService.delete(userId);
        UserStatus userStatus = userStatusService.findByUserId(userId);
        userStatusService.delete(userStatus.getId());

        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
