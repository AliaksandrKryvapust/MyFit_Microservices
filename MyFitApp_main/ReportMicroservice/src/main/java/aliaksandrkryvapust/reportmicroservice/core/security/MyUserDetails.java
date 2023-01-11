package aliaksandrkryvapust.reportmicroservice.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class MyUserDetails implements UserDetails {
    private final UserPrincipal userPrincipal;

    public MyUserDetails(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    public UUID getId(){
        return userPrincipal.getId();
    }

    public Instant getVersion(){
        return userPrincipal.getDtUpdate();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority(userPrincipal.getRole().name()));
    }

    @Override
    public String getPassword() {
        return "qwerty";
    }

    @Override
    public String getUsername() {
        return userPrincipal.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userPrincipal.getAuthenticated();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userPrincipal.getAuthenticated();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userPrincipal.getAuthenticated();
    }

    @Override
    public boolean isEnabled() {
        return userPrincipal.getAuthenticated();
    }
}
