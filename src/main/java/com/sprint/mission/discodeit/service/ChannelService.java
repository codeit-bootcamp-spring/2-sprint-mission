package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 채널(Channel)을 관리하는 서비스 인터페이스입니다.
 * 채널의 생성, 조회, 참가, 탈퇴, 수정 및 삭제 기능을 제공합니다.
 */
@Service
public interface ChannelService {
    /**
     * 채널 시스템을 초기화합니다. 관리자의 인증 여부에 따라 수행됩니다.
     *
     * @param adminAuth 관리자로 인증된 경우 {@code true}, 그렇지 않으면 {@code false}
     */
    void reset(boolean adminAuth);
    /**
     * 새로운 채널을 생성하고 고유한 채널 ID(UUID)를 반환합니다.
     *
     * @param channelCRUDDTO 생성할 채널 정보 DTO
     * @return 생성된 채널 객체
     */
    Channel create(ChannelCRUDDTO channelCRUDDTO);
    /**
     * 기존 채널에 참가하고 참가한 채널의 고유 ID(UUID)를 반환합니다.
     *
     * @param channelCRUDDTO 참가할 채널 정보 DTO
     * @return 참가한 유저
     */
    User join(ChannelCRUDDTO channelCRUDDTO);
    /**
     * 특정 채널에서 사용자를 탈퇴시키고 탈퇴한 채널의 고유 ID(UUID)를 반환합니다.
     *
     * @param channelCRUDDTO 탈퇴할 채널 정보 DTO
     * @return 탈퇴한 유저 객체
     */
    User quit(ChannelCRUDDTO channelCRUDDTO);
    /**
     * 특정 채널 ID를 기반으로 채널 정보를 조회합니다.
     *
     * @param channelId 조회할 채널의 ID
     * @return 조회된 채널 정보를 포함한 {@link ChannelDTO} 객체
     */
    ChannelDTO find(String channelId);
    /**
     * 특정 서버 내에서 사용자가 속한 모든 채널을 조회합니다.
     *
     * @param serverId 조회할 서버의 ID
     * @return 해당 서버에서 사용자가 속한 채널 목록
     */
    List<ChannelDTO> findAllByServerAndUser(String serverId );
    /**
     * 특정 서버 내 모든 채널 정보를 출력합니다.
     *
     * @param serverId 출력할 채널이 속한 서버의 ID
     * @return 출력 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean printChannels(String serverId);
    /**
     * 특정 채널에 속한 모든 사용자의 정보를 출력합니다.
     *
     * @param channelId 출력할 채널의 ID
     * @return 출력 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean printUsersInChannel(String channelId);
    /**
     * 특정 채널을 삭제합니다.
     *
     * @param channelCRUDDTO 삭제할 채널 정보 DTO
     * @return 삭제 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean delete(ChannelCRUDDTO channelCRUDDTO);
    /**
     * 특정 채널의 정보를 업데이트합니다.
     *
     * @param channelCRUDDTO     기존 채널 정보 DTO
     * @param channelUpdateDTO   업데이트할 채널 정보 DTO
     * @return 업데이트 성공 여부, 성공하면 {@code true}, 실패하면 {@code false}
     */
    boolean update(ChannelCRUDDTO channelCRUDDTO, ChannelCRUDDTO channelUpdateDTO);

}
