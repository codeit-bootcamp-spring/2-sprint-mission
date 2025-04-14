package com.sprint.discodeit.sprint.service.basic.users;

import com.sprint.discodeit.sprint.domain.StatusType;
import com.sprint.discodeit.sprint.domain.dto.UsersIdAllResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersNameStatusResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersProfileImgResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersRequestDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersResponseDto;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersUpdateRequestDto;
import com.sprint.discodeit.sprint.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint.domain.entity.Users;
import com.sprint.discodeit.sprint.domain.entity.UsersStatus;
import com.sprint.discodeit.sprint.domain.mapper.UsersMapper;
import com.sprint.discodeit.sprint.global.ErrorCode;
import com.sprint.discodeit.sprint.global.RequestException;
import com.sprint.discodeit.sprint.repository.UserStatusRepository;
import com.sprint.discodeit.sprint.repository.UsersRepository;
import com.sprint.discodeit.sprint.service.basic.util.BinaryGenerator;
import com.sprint.discodeit.sprint.service.basic.util.UsersStatusEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository userRepository;
    private final BinaryGenerator binaryGenerator;
    private final UsersStatusEvaluator usersStatusEvaluator;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UsersNameStatusResponseDto create(UsersRequestDto usersRequestDto, UsersProfileImgResponseDto usersProfileImgResponseDto) {
        if (usersRequestDto == null) {
            throw new RequestException(ErrorCode.users_REQUEST_NULL);
        }
        if (usersProfileImgResponseDto == null) {
            throw new RequestException(ErrorCode.PROFILE_IMAGE_NULL);
        }

        if (userRepository.findByUsername(usersRequestDto.usersname()).isPresent()) {
            throw new RequestException(ErrorCode.DUPLICATE_usersNAME);
        }

        if (userRepository.findByEmail(usersRequestDto.email()).isPresent()) {
            throw new RequestException(ErrorCode.DUPLICATE_EMAIL);
        }

        BinaryContent profileImage = binaryGenerator.createProfileImage(usersProfileImgResponseDto.imgUrl());
        Users users = UsersMapper.tousers(usersRequestDto);

        users.addBinaryContent(profileImage);
        userRepository.save(users);

        return new UsersNameStatusResponseDto(users.getUsername(), StatusType.Active.toString(), users.getId());
    }

    @Override
    public UsersResponseDto find(Long usersId) {
        Users users = userRepository.findById(usersId)
                .orElseThrow(() -> new RequestException(ErrorCode.users_NOT_FOUND));
        String status = usersStatusEvaluator.determineusersStatus(users.getLastModified());
        return new UsersResponseDto(users.getId(), users.getUsername(), users.getEmail(), status);
    }

    @Override
    public UsersIdAllResponseDto findAll() {
        return new UsersIdAllResponseDto(userRepository.findAllUserIds());
    }

    @Override
    public UsersResponseDto update(UsersUpdateRequestDto usersUpdateRequestDto, Long usersId) {
        Users users = userRepository.findById(usersId)
                .orElseThrow(() -> new RequestException(ErrorCode.users_NOT_FOUND));

        BinaryContent profileImage = binaryGenerator.createProfileImage(usersUpdateRequestDto.profileImg());

        users.addBinaryContent(profileImage);

        users.update(usersUpdateRequestDto.newusersname(), usersUpdateRequestDto.newEmail(), usersUpdateRequestDto.newPassword());
        userRepository.save(users);

        return new UsersResponseDto(users.getId(), users.getUsername(), users.getEmail(), StatusType.Active.toString());
    }

    @Override
    public void delete(Long usersId) {
        Users user = userRepository.findById(usersId)
                .orElseThrow(() -> new RequestException(ErrorCode.ALREADY_DELETED_users));
        user.deactivate();
        userRepository.save(user);
    }

    @Override
    public usersLoginResponseDto login(usersLoginRequestDto usersLoginRequestDto) {
            Users users = fileusersRepository.findByusersname(usersLoginRequestDto.usersname())
                    .orElseThrow(() -> new AuthException(ErrorCode.UNAUTHORIZED));

            if (!users.getPassword().equals(usersLoginRequestDto.password())) {
                throw new AuthException(ErrorCode.UNAUTHORIZED);
            }
            return new usersLoginResponseDto(users.getId().toString(), users.getUsersname());
        }

}
