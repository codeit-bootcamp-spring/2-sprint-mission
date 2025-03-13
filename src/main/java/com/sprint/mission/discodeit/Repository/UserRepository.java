package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.Exception.ServerNotFoundException;
import com.sprint.mission.discodeit.Exception.UserNotFoundException;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * 사용자 및 서버를 관리하는 서비스 인터페이스.
 * <p>
 * 이 인터페이스는 사용자의 생성, 조회, 수정, 삭제 및 사용자가 관리하는 서버의
 * 생성, 조회, 수정, 삭제 기능을 제공한다.
 * </p>
 *
 * @author ChatGPT
 * @version 1.0
 */

public interface UserRepository {
    /**
     * 새로운 사용자를 저장합니다.
     *
     * @param user 저장할 사용자 객체
     * @return 생성된 사용자의 고유 식별자 (UUID)
     * @throws IllegalArgumentException 유효하지 않은 사용자 정보가 입력된 경우
     */
    UUID saveUser(User user);

    /**
     * 사용자가 새로운 서버를 저장합니다.
     *
     * @param user 서버를 생성하는 사용자
     * @param server 저장할 서버 객체
     * @return 생성된 서버의 고유 식별자 (UUID)
     * @throws IllegalArgumentException 유효하지 않은 서버 정보가 입력된 경우
     */
    UUID saveServer(User user, Server server);

    /**
     * 특정 사용자를 조회합니다.
     *
     * @param user 조회할 사용자 객체
     * @return 조회된 사용자 객체
     * @throws UserNotFoundException 사용자가 존재하지 않는 경우
     */
    User findUser(User user);

    /**
     * 사용자 ID(UUID)를 통해 특정 사용자를 조회합니다.
     *
     * @param userId 조회할 사용자의 UUID
     * @return 조회된 사용자 객체
     * @throws UserNotFoundException 사용자가 존재하지 않는 경우
     */
    User findUserByUserId(UUID userId);

    /**
     * 특정 사용자가 소유한 서버 목록을 조회합니다.
     *
     * @param owner 서버 소유자 (User)
     * @return 사용자가 소유한 서버 목록
     * @throws ServerNotFoundException 사용자가 소유한 서버가 없는 경우
     */
    List<Server> findServerListByOwner(User owner);
    /**
     * 특정 사용자가 소유한 서버 중 특정 서버를 조회합니다.
     *
     * @param owner       서버 소유자 (User)
     * @param targetServer 조회할 서버 객체
     * @return 조회된 서버 객체
     * @throws ServerNotFoundException 해당 서버가 존재하지 않는 경우
     */
    Server findServerByOwner(User owner, Server targetServer);
    /**
     * 서버 ID(UUID)를 통해 특정 서버를 조회합니다.
     *
     * @param owner    서버 소유자 (User)
     * @param serverId 조회할 서버의 UUID
     * @return 조회된 서버 객체
     * @throws ServerNotFoundException 해당 서버가 존재하지 않는 경우
     */
    Server findServerByServerId(User owner, UUID serverId);
    /**
     * 사용자의 이름을 변경합니다.
     *
     * @param user        변경할 사용자 객체
     * @param replaceName 변경할 새로운 이름
     * @return 변경된 사용자의 UUID
     * @throws UserNotFoundException       사용자가 존재하지 않는 경우
     */
    UUID updateUserName(User user, String replaceName);
    /**
     * 특정 서버의 이름을 변경합니다.
     *
     * @param owner       서버 소유자 (User)
     * @param server      변경할 서버 객체
     * @param replaceName 변경할 새로운 서버 이름
     * @return 변경된 서버의 UUID
     * @throws ServerNotFoundException     해당 서버가 존재하지 않는 경우
     */
    UUID updateServerName(User owner, Server server, String replaceName);
    /**
     * 특정 사용자를 삭제합니다.
     *
     * @param user 삭제할 사용자 객체
     * @return 삭제된 사용자의 UUID
     * @throws UserNotFoundException       사용자가 존재하지 않는 경우
     */
    UUID removeUser(User user);
    /**
     * 특정 사용자가 소유한 서버를 삭제합니다.
     *
     * @param owner  서버 소유자 (User)
     * @param server 삭제할 서버 객체
     * @return 삭제된 서버의 UUID
     * @throws ServerNotFoundException     해당 서버가 존재하지 않는 경우
     */
    UUID removeServer(User owner, Server server);

}
