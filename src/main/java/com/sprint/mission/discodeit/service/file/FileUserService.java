package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;
import static com.sprint.mission.discodeit.constants.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.discodeit.constants.FilePath.USER_FILE;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserService implements UserService {
    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        User requestUser = new User(userRegisterDto.name(), userRegisterDto.email(), userRegisterDto.password());

        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());
        users.values()
                .stream()
                .filter(existingUser -> existingUser.isSameEmail(requestUser.getEmail()))
                .findFirst()
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 존재하는 유저입니다.");
                });

        users.put(requestUser.getId(), requestUser);
        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), USER_FILE.getPath(), users);

        return toDto(requestUser);
    }

    @Override
    public UserDto findById(UUID id) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        User user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent());
        }

        return toDto(user);
    }

    @Override
    public List<UserDto> findByName(String name) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        return users.values()
                .stream()
                .filter(user -> user.isSameName(name))
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findAll() {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        return users.values()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDto findByEmail(String email) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        return users.values()
                .stream()
                .filter(user -> user.isSameEmail(email))
                .map(this::toDto)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));
    }

    @Override
    public List<UserDto> findAllByIds(List<UUID> userIds) {
        return userIds
                .stream()
                .map(this::findById)
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        User user = users.get(findById(id).id());
        user.updateName(name);
        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), USER_FILE.getPath(), users);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> users = loadObjectsFromFile(USER_FILE.getPath());

        users.remove(id);
        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), USER_FILE.getPath(), users);
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
