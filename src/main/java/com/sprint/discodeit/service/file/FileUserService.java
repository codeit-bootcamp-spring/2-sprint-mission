package com.sprint.discodeit.service.file;

import com.sprint.discodeit.domain.dto.UserProfileImgResponseDto;
import com.sprint.discodeit.domain.dto.UserNameResponse;
import com.sprint.discodeit.domain.mapper.UserMapper;
import com.sprint.discodeit.domain.dto.UserRequestDto;
import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.repository.UserRepository;
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

    public UserNameResponse create(UserRequestDto userRequestDto, UserProfileImgResponseDto userProfileImgResponseDto) {
        // 중복된 유저 확인
        if (fileUserRepository.findByUsername(userRequestDto.username()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다. 확인 해주세요.");
        }

        // 이메일과 닉네임이 동일한지 확인
        if (userRequestDto.username().equals(userRequestDto.email())) {
            throw new IllegalArgumentException("이메일과 닉네임이 동일합니다. 수정해주세요.");
        }

        //  User 객체 생성
        User user = new User(userRequestDto.username(), userRequestDto.email(), userRequestDto.password());
        fileUserRepository.save(user);

        // User -> UserNameResponse 변환
        return UserMapper.toUserNameResponse(user.getUsername());
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
