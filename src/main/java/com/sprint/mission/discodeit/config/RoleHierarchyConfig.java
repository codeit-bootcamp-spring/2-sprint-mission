package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class RoleHierarchyConfig {

    @Bean
    RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role(Role.ROLE_ADMIN.name()).implies(Role.ROLE_CHANNEL_MANAGER.name())
            .role(Role.ROLE_CHANNEL_MANAGER.name()).implies(Role.ROLE_USER.name())
            .build();
    }
}
