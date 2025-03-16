package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.Repository.ReadStatusRepository;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ServerRepository serverRepository;

    void create(ReadStatusCreateDTO readStatusCreateDTO) {
        try {
            UUID userId = UUID.fromString(readStatusCreateDTO.userId());
            UUID channelId = UUID.fromString(readStatusCreateDTO.channelID());

            User user = userRepository.findUserByUserId(userId);



        } catch (UserNotFoundException e) {
            System.out.println("Read Status: 해당 유저는 존재하지 않습니다.");
        }
    }

    void find(){

    }

    void findAllByUserId(){

    }

    void update(){

    }

    void delete(){
        
    }
}
