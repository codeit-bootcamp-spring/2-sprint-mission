package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.Exception.*;
import com.sprint.mission.discodeit.entity.Server;

import java.util.UUID;

/**
 * 사용자 서비스 인터페이스.
 * <p>
 * 사용자가 서버를 생성, 가입, 조회, 삭제, 수정할 수 있도록 제공하는 서비스.
 * </p>
 *
 * @author ChatGPT
 * @version 1.0
 */
public interface UserService {
    /**
     * 유저를 등록합니다.
     *
     * @param userName 가입할 유저 이름
     * @param password 가입할 유저의 password
     * @return 가입한 유저의 고유 식별자(UUID)
     * */
    UUID registerUser(String userName, String password);
    /**
     * 유저를 로그인합니다.
     *
     * @param userId 로그인할 유저 ID(UUID)
     * @param password 로그인할 유저의 비밀번호
     * @return 로그인 성공 여부 (true = 로그인 성공, false = 실패)
     * @throws UserNotFoundException 해당 ID의 사용자가 존재하지 않는 경우
     * @throws InvalidPasswordException 비밀번호가 일치하지 않는 경우
     */
    boolean loginUser(String userId, String password);


    /**
     * 새로운 서버를 생성합니다.
     *
     * @param userOwnerId 서버를 생성할 유저 ID(UUID)
     * @param name 생성할 서버의 이름
     * @return 생성된 서버의 고유 식별자 (UUID)
     * @throws UserNotFoundException 해당 ID의 사용자가 존재하지 않는 경우
     */
    UUID createServer(String userOwnerId, String name);

    /**
     * 사용자를 특정 서버에 가입시킵니다.
     *
     * @param userId 가입할 사용자 ID(UUID)
     * @param serverId 가입할 서버 ID(UUID)
     * @return 가입 성공한 서버의 고유 식별자 (UUID)
     * @throws UserNotFoundException 해당 ID의 사용자가 존재하지 않는 경우
     * @throws ServerNotFoundException 해당 ID의 서버가 존재하지 않는 경우
     */
    UUID joinServer(String userId, String serverId);

    /**
     * 특정 서버를 조회합니다.
     *
     * @param userId 조회할 사용자 ID(UUID)
     * @param name 조회할 서버 이름
     * @return 조회된 서버 객체
     * @throws UserNotFoundException 해당 ID의 사용자가 존재하지 않는 경우
     * @throws ServerNotFoundException 해당 이름의 서버가 존재하지 않는 경우
     * @throws UnauthorizedAccessException 사용자가 해당 서버에 가입되지 않은 경우
     */
    Server getServer(String userId, String name);


    /**
     * 사용자가 속한 서버 정보를 출력합니다.
     *
     * @param userId 조회할 사용자 ID(UUID)
     * @throws UserNotFoundException 해당 ID의 사용자가 존재하지 않는 경우
     * @throws EmptyServerListException 사용자가 가입한 서버가 없는 경우
     */
    void printServer(String userId);

    /**
     * 특정 사용자를 삭제합니다.
     *
     * @param userId 삭제 요청 사용자 ID(UUID)
     * @return 삭제 성공 여부 (true = 삭제 성공, false = 실패)
     * @throws UserNotFoundException 해당 ID의 사용자가 존재하지 않는 경우
     * @throws UnauthorizedAccessException 사용자가 삭제 권한이 없는 경우
     */
    boolean removeUser(String userId);

    /**
     * 특정 서버를 삭제합니다.
     *
     * @param ownerId 삭제 요청 서버 주인장 ID(UUID)
     * @param serverId 삭제 요청 서버 ID(UUID)
     * @return 삭제 성공 여부 (true = 삭제 성공, false = 실패)
     * @throws UserNotFoundException 해당 ID의 사용자가 존재하지 않는 경우
     * @throws ServerNotFoundException 해당 ID의 서버가 존재하지 않는 경우
     * @throws UnauthorizedAccessException 사용자가 서버 삭제 권한이 없는 경우
     */
    boolean removeServer(String ownerId, String serverId);

    /**
     * 유저의 이름을 변경합니다.
     *
     * @param userId 요청 사용자 ID(UUID)
     * @param replaceName 변경할 새로운 이름
     * @return 변경 성공 여부 (true = 변경 성공, false = 실패)
     * @throws UserNotFoundException 해당 ID의 사용자가 존재하지 않는 경우
     */
    boolean updateUser(String userId, String replaceName);

    /**
     * 특정 서버의 이름을 변경합니다.
     *
     * @param ownerId 요청 서버 주인장 ID(UUID)
     * @param serverId 변경할 대상 서버 ID(UUID)
     * @param replaceName 변경할 새로운 이름
     * @return 변경 성공 여부 (true = 변경 성공, false = 실패)
     * @throws UserNotFoundException 해당 ID의 사용자가 존재하지 않는 경우
     * @throws ServerNotFoundException 해당 ID의 서버가 존재하지 않는 경우
     */
    boolean updateServer(String ownerId, String serverId, String replaceName);
}
