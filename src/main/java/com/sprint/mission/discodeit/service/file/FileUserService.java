package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusRequest;
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
import java.util.stream.Stream;

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

    @Override
    public User create(UserCreateRequest createRequest,
                       Optional<BinaryContentCreateRequest> binaryContentRequestNullable) {
        validDuplicateUsername(createRequest.username());
        validDuplicateEmail(createRequest.email());
        UUID binaryContentId = binaryContentRequestNullable
                .map(basicBinaryContentService::create).map(BinaryContent::getId).orElse(null);
        User user = new User(createRequest.username(), createRequest.email()
                , createRequest.password(), binaryContentId);

        UserStatusRequest statusParam = new UserStatusRequest(user.getId());
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
                user.getEmail(), user.getProfileId(), userStatus.isOnline());
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
                                userStatus.isOnline()
                        );
                    }).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User update(UserUpdateRequest updateRequest,
                       Optional<BinaryContentCreateRequest> binaryContentRequestNullable) {
        User userNullable = null;

        Path path = resolvePath(updateRequest.id());
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
                .orElseThrow(() -> new NoSuchElementException("User with id " + updateRequest.id() + " not found"));

        String username = (updateRequest.newUsername() == null) ? user.getUsername() : updateRequest.newUsername();
        String email = (updateRequest.newEmail() == null) ? user.getEmail() : updateRequest.newEmail();
        String password = (updateRequest.newPassword() == null) ? user.getPassword() : updateRequest.newPassword();

        validDuplicateUsername(username);
        validDuplicateEmail(email);
        UUID binaryContentId = binaryContentRequestNullable
                .map(request -> {
                    if (user.getProfileId() != null) {
                        basicBinaryContentService.delete(user.getProfileId());
                    }
                    return basicBinaryContentService.create(request);
                }).map(BinaryContent::getId).orElse(null);

        user.update(username, email, password, binaryContentId);
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

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .flatMap(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            User user = (User) ois.readObject();
                            return user.getUsername().equals(username) ? Stream.of(user) : Stream.empty();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    private boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(u -> u.username().equals(username));
    }

    private boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(u -> u.email().equals(email));
    }

    private void validDuplicateUsername(String username) {
        if (existsByUsername(username)) {
            throw new IllegalArgumentException(username + " 은 중복된 username.");
        }
    }

    private void validDuplicateEmail(String email) {
        if (existsByEmail(email)) {
            throw new IllegalArgumentException(email + " 은 중복된 email.");
        }
    }
}
