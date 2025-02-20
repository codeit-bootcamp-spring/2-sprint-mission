package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.application.UserDto;
import com.sprint.mission.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> users = new ArrayList<>();

    @Override
    public UserDto findById(UUID id) {
        return users.stream()
                .filter(user -> user.isSameId(id))
                .findFirst()
                .map(user -> new UserDto(user.getId(), user.getName()))
                .orElseThrow(() -> new IllegalArgumentException("[Error] 해당 아이디의 유저가 없습니다"));
    }

    @Override
    public List<UserDto> findByName(String name) {
        return users.stream()
                .filter(user -> user.isSameName(name))
                .map(user -> new UserDto(user.getId(), user.getName()))
                .toList();
    }

    @Override
    public List<UserDto> findAll() {
        return users.stream()
                .map(user -> new UserDto(user.getId(), user.getName()))
                .toList();
    }

    @Override
    public void register(UserRegisterDto userRegisterDto) {
        users.add(new User(userRegisterDto.name(), userRegisterDto.password()));
    }
}
