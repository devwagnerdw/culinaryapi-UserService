package com.culinaryapi.culinaryapi_user_Service.utils;

import com.culinaryapi.culinaryapi_user_Service.configs.security.AuthenticationCurrentUserService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PermissionUtils {

    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    public PermissionUtils(AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    public boolean hasPermission(UUID targetUserId) {
        var currentUser = authenticationCurrentUserService.getCurrentUser();
        UUID currentUserId = currentUser.getUserId();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin || currentUserId.equals(targetUserId);
    }
}