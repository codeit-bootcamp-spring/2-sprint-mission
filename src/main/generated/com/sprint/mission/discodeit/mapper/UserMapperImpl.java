package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-09T10:41:21+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (JetBrains s.r.o.)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Autowired
    private BinaryContentMapper binaryContentMapper;

    @Override
    public List<UserDto> toDto(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( toDto( user ) );
        }

        return list;
    }

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        BinaryContentDto profile = null;
        UUID id = null;
        String username = null;
        String email = null;

        profile = binaryContentMapper.toDto( user.getProfile() );
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();

        Boolean online = user.getUserStatus() != null && user.getUserStatus().isOnline();

        UserDto userDto = new UserDto( id, username, email, profile, online );

        return userDto;
    }
}
