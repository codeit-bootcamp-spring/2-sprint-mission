package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class CreateUserDTO {
    private String username;
    private String password;
    private String email;
    //
    private byte[] image;
    private String imageContentType;
}
