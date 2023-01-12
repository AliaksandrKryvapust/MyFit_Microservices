package itacad.aliaksandrkryvapust.usermicroservice.service;

import itacad.aliaksandrkryvapust.usermicroservice.repository.api.IUserRepository;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = this.userRepository.findByEmail(email);
        this.validate(email, user);
        boolean enabled = user.getStatus().equals(UserStatus.ACTIVATED);
        boolean nonLocked = !user.getStatus().equals(UserStatus.DEACTIVATED);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), enabled,
                true, true, nonLocked, authorityList);
    }

    private void validate(String email, User user) {
        if (user == null) {
            throw new NoSuchElementException("There is no such user" + email);
        }
    }
}
