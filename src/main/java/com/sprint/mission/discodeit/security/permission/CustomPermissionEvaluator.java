package com.sprint.mission.discodeit.security.permission;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import java.io.Serializable;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("customPermissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject,
        Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
        String targetType, Object permission) {
        if (!(authentication.getPrincipal() instanceof DiscodeitUserDetails userDetails)) {
            return false;
        }
        UUID loginId = userDetails.getUserDto().id();

        String action = permission.toString();

        return switch (targetType) {
            case "User" ->
                loginId.equals(targetId) || userDetails.getUserDto().role() == Role.ADMIN;

            case "Message" -> messageRepository.findById((UUID) targetId)
                .map(message -> {
                    UUID authorId = message.getAuthor().getId();
                    return switch (action) {
                        case "update" -> authorId.equals(loginId);
                        case "delete" -> authorId.equals(loginId)
                            || userDetails.getUserDto().role() == Role.ADMIN;
                        default -> false;
                    };
                }).orElse(false);

            case "ReadStatus" -> switch (action) {
                case "create" -> loginId.equals(targetId);
                case "update" -> readStatusRepository.findById((UUID) targetId)
                    .map(readStatus -> readStatus.getUser().getId().equals(loginId))
                    .orElse(false);
                default -> false;
            };
            default -> false;
        };
    }
}
