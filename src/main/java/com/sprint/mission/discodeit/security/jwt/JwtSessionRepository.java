package com.sprint.mission.discodeit.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JwtSessionRepository extends JpaRepository<JwtSession, Long> {

    Optional<JwtSession> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("DELETE FROM JwtSession js WHERE js.user.id = :userId")
    void deleteByUserId(@Param("userId") UUID userId);

    @Query("SELECT js FROM JwtSession js WHERE js.user.id = :userId")
    List<JwtSession> findAllByUserId(@Param("userId") UUID userId);


    @Query("SELECT COUNT(js) > 0 FROM JwtSession js WHERE js.user.id = :userId")
    boolean existsByUserId(@Param("userId") UUID userId);


}