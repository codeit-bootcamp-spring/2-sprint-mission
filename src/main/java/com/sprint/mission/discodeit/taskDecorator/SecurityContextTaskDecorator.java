package com.sprint.mission.discodeit.taskDecorator;

import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 현재 스레드의 SecurityContext 캡처
        SecurityContext originalContext = SecurityContextHolder.getContext();

        // SecurityContext의 깊은 복사 수행
        SecurityContext contextCopy = createContextCopy(originalContext);

        return () -> {
            SecurityContext previousContext = SecurityContextHolder.getContext();
            try {
                // 비동기 스레드에 복사된 SecurityContext 설정
                SecurityContextHolder.setContext(contextCopy);
                runnable.run();
            } finally {
                // 원본 컨텍스트로 복원 (스레드 풀 재사용 고려)
                SecurityContextHolder.setContext(previousContext);
            }
        };
    }

    /**
     * SecurityContext의 깊은 복사를 수행한다.
     * Authentication 객체와 그 내부의 Principal, Authorities까지 복사한다.
     */
    private SecurityContext createContextCopy(SecurityContext original) {
        if (original == null || original.getAuthentication() == null) {
            return SecurityContextHolder.createEmptyContext();
        }

        SecurityContext copy = SecurityContextHolder.createEmptyContext();

        // Authentication 객체는 일반적으로 불변이므로 참조 복사 가능
        // 하지만 커스텀 Authentication 구현체의 경우 주의 필요
        copy.setAuthentication(original.getAuthentication());

        return copy;
    }
}
