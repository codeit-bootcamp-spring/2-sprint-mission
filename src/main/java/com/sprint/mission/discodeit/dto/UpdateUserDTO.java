package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserDTO {
    private String username;
    private String email;
    private String password;
    private UUID userId;
    //
    private byte[] newImage;
    private String newImageContentType;
}
