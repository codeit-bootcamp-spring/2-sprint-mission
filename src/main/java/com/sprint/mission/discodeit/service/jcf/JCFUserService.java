package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.infra.UserRepository;
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

        return new UserDto(savedUser.getId(), savedUser.getName());
    }

    @Override
    public UserDto findById(UUID id) {
        User user = userRepository.findById(id);

        return new UserDto(user.getId(), user.getName());
    }

    @Override
    public List<UserDto> findByName(String name) {
        return userRepository.findByName(name)
                .stream()
                .map(user -> new UserDto(user.getId(), user.getName()))
                .toList();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDto(user.getId(), user.getName()))
                .toList();
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);

        return new UserDto(user.getId(), user.getName());
    }

    @Override
    public void updateName(UUID id, String name) {
        userRepository.updateName(id, name);
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
