package com.sprint.discodeit.sprint.service.basic.users;

import com.sprint.discodeit.sprint.domain.StatusType;
import com.sprint.discodeit.sprint.domain.dto.usersDto.*;
import com.sprint.discodeit.sprint.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint.domain.entity.users;
import com.sprint.discodeit.sprint.domain.entity.usersStatus;
import com.sprint.discodeit.sprint.domain.mapper.usersMapper;
import com.sprint.discodeit.sprint.global.AuthException;
import com.sprint.discodeit.sprint.global.ErrorCode;
import com.sprint.discodeit.sprint.global.RequestException;
import com.sprint.discodeit.sprint.repository.file.BaseBinaryContentRepository;
import com.sprint.discodeit.sprint.repository.file.BaseUsersStatusRepository;
import com.sprint.discodeit.sprint.repository.file.FileUsersRepository;
import com.sprint.discodeit.sprint.service.basic.util.BinaryGenerator;
import com.sprint.discodeit.sprint.service.basic.util.usersStatusEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUsersService implements UsersServiceV1 {

    private final FileUsersRepository fileusersRepository;
    private final BinaryGenerator binaryGenerator;
    private final usersStatusEvaluator usersStatusEvaluator;
    private final BaseUsersStatusRepository baseusersStatusRepository;
    private final BaseBinaryContentRepository baseBinaryContentRepository;

    @Override
    public usersNameStatusResponseDto create(usersRequestDto usersRequestDto, usersProfileImgResponseDto usersProfileImgResponseDto) {
        if (usersRequestDto == null) {
            throw new RequestException(ErrorCode.users_REQUEST_NULL);
        }
        if (usersProfileImgResponseDto == null) {
            throw new RequestException(ErrorCode.PROFILE_IMAGE_NULL);
        }

        if (fileusersRepository.findByusersname(usersRequestDto.usersname()).isPresent()) {
            throw new RequestException(ErrorCode.DUPLICATE_usersNAME);
        }

        if (fileusersRepository.findByEmail(usersRequestDto.email()).isPresent()) {
            throw new RequestException(ErrorCode.DUPLICATE_EMAIL);
        }

        BinaryContent profileImage = binaryGenerator.createProfileImage(usersProfileImgResponseDto.imgUrl());
        users users = usersMapper.tousers(usersRequestDto);
        usersStatus usersStatus = new usersStatus(users.getCreatedAt(), StatusType.Active.getExplanation());

        users.associateStatus(usersStatus);
        users.associateProfileId(profileImage);

        baseusersStatusRepository.save(usersStatus);
        fileusersRepository.save(users);
        baseBinaryContentRepository.save(profileImage);

        return new usersNameStatusResponseDto(users.getUsersname(), usersStatus.getStatusType(), users.getId());
    }

    @Override
    public usersResponseDto find(UUID usersId) {
        users users = fileusersRepository.findById(usersId)
                .orElseThrow(() -> new RequestException(ErrorCode.users_NOT_FOUND));

        usersStatus usersStatus = baseusersStatusRepository.findById(users.getUsersStatusId())
                .orElseThrow(() -> new RequestException(ErrorCode.users_STATUS_NOT_FOUND));

        String status = usersStatusEvaluator.determineusersStatus(usersStatus.getLastLoginTime());

        return new usersResponseDto(users.getProfileId(), users.getUsersname(), users.getEmail(), status);
    }

    @Override
    public List<users> findAll() {
        return fileusersRepository.findByAll();
    }

    @Override
    public usersResponseDto update(usersUpdateRequestDto usersUpdateRequestDto, String usersId) {
        users users = fileusersRepository.findById(UUID.fromString(usersId))
                .orElseThrow(() -> new RequestException(ErrorCode.users_NOT_FOUND));

        BinaryContent profileImage = binaryGenerator.createProfileImage(usersUpdateRequestDto.profileImg());

        users.associateProfileId(profileImage);
        baseBinaryContentRepository.save(profileImage);

        users.update(usersUpdateRequestDto.newusersname(), usersUpdateRequestDto.newEmail(), usersUpdateRequestDto.newPassword());
        fileusersRepository.save(users);

        return new usersResponseDto(users.getProfileId(), users.getUsersname(), users.getEmail(), StatusType.Active.toString());
    }

    @Override
    public void delete(UUID usersId) {
        Optional<users> users = fileusersRepository.findById(usersId);
        if (users.isPresent()) {
            users.get().softDelete();
            usersStatus usersStatus = new usersStatus(Instant.now(), StatusType.Inactive.getExplanation());
            baseusersStatusRepository.save(usersStatus);
            fileusersRepository.save(users.get());
        } else {
            throw new RequestException(ErrorCode.ALREADY_DELETED_users);
        }
    }

    @Override
    public usersLoginResponseDto login(usersLoginRequestDto usersLoginRequestDto) {
            users users = fileusersRepository.findByusersname(usersLoginRequestDto.usersname())
                    .orElseThrow(() -> new AuthException(ErrorCode.UNAUTHORIZED));

            if (!users.getPassword().equals(usersLoginRequestDto.password())) {
                throw new AuthException(ErrorCode.UNAUTHORIZED);
            }
            return new usersLoginResponseDto(users.getId().toString(), users.getUsersname());
        }

}
