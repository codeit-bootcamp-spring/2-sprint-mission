package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.application.UserDto;
import com.sprint.mission.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public UserDto findById(UUID id) {
        User user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException("[Error] 해당 아이디의 유저가 없습니다");
        }

        return new UserDto(user.getId(), user.getName());
    }

    @Override
    public List<UserDto> findByName(String name) {
        return users.values().stream()
                .filter(user -> user.isSameName(name))
                .map(user -> new UserDto(user.getId(), user.getName()))
                .toList();
    }

    @Override
    public List<UserDto> findAll() {
        return users.values().stream()
                .map(user -> new UserDto(user.getId(), user.getName()))
                .toList();
    }

    @Override
    public void register(UserRegisterDto userRegisterDto) {
        UUID id = UUID.randomUUID(); // TODO: 2/20/25 Id가 UUID에 의존하지 않도록 수정 요망
        users.put(id, new User(id, userRegisterDto.name(), userRegisterDto.password()));
    }
}
