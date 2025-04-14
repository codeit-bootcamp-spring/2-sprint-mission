package com.sprint.discodeit.sprint.service.basic.users;


import com.sprint.discodeit.sprint.domain.StatusType;
import com.sprint.discodeit.sprint.domain.dto.usersDto.UsersStatusResponseDto;
import com.sprint.discodeit.sprint.domain.entity.Users;
import com.sprint.discodeit.sprint.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersStatusService {

    private final UsersRepository usersRepository;

    public UsersStatusResponseDto creat(Long usersId, StatusType statusType){
        Users users = usersRepository.findById(usersId)
                .orElseThrow(() -> new IllegalArgumentException("Users not found"));
        users.addUpdateStatus(statusType);
        usersRepository.save(users);
        return new UsersStatusResponseDto(users.getId(), users.getUsername(), statusType.getExplanation());
    }
}
