package com.ijse.projecttracker.auth.util;

import com.ijse.projecttracker.auth.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

public class SecurityUtils {
    public static Optional<Long> getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || ! (auth.getPrincipal() instanceof UserDetailsImpl)) return Optional.empty();
        return Optional.of(((UserDetailsImpl) auth.getPrincipal()).getId());
    }
}
