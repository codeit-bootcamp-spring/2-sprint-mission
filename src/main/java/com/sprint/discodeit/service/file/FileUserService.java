package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.dto.UserNameResponse;
import com.sprint.discodeit.domain.mapper.UserMapper;
import com.sprint.discodeit.domain.dto.UserRequestDto;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.repository.file.FileUserRepository;
import com.sprint.discodeit.service.UserServiceV1;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUserService implements UserServiceV1 {

    private final FileUserRepository fileUserRepository;

    @Override
    public UserNameResponse create(UserRequestDto userRequestDto) {
        User userMapper = UserMapper.toUserMapper(userRequestDto);
        fileUserRepository.save(userMapper);
        return UserMapper.toUserNameResponse(userMapper.getUsername());
    }

    @Override
    public User find(UUID userId) {
        User byId = fileUserRepository.findById(userId.toString());
        return byId;
    }

    @Override
    public List<User> findAll() {
        return fileUserRepository.findByAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = fileUserRepository.findById(userId.toString());
        user.update(newUsername, newEmail, newPassword);
        fileUserRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        fileUserRepository.delete(userId);
    }
}
