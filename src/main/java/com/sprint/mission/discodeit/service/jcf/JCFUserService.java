package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.constants.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        User savedUser = userRepository.save(
                new User(userRegisterDto.name(), userRegisterDto.email(), userRegisterDto.password())
        );

        return toDto(savedUser);
    }

    @Override
    public UserDto findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        return toDto(user);
    }

    @Override
    public List<UserDto> findByName(String name) {
        return userRepository.findByName(name)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));

        return toDto(user);
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
        userRepository.updateName(id, name);
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
