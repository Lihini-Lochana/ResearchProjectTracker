package com.ijse.projecttracker.auth.security;

import com.ijse.projecttracker.auth.entity.User;
import com.ijse.projecttracker.auth.entity.UserStatus;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Long batchId;

    public UserDetailsImpl(Long id, String username, String password, boolean enabled,
                           Collection<? extends GrantedAuthority> authorities, Long batchId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
        this.batchId = batchId;
    }

    public static UserDetailsImpl fromUser(User user) {
        var auths = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                .collect(Collectors.toList());
        boolean enabled = user.isEmailVerified() && user.getStatus() == UserStatus.ACTIVE;
        Long batchId = user.getBatch() != null ? user.getBatch().getId() : null;

        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPasswordHash(), enabled, auths, batchId);
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}

