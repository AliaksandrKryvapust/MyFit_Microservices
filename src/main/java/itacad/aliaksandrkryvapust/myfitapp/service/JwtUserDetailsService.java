package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.repository.api.IUserRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final IUserRepository dao;

    @Autowired
    public JwtUserDetailsService(IUserRepository dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = this.dao.findByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException("There is no such user" + username);
        }
        List<GrantedAuthority> authorityList =  new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER_ROLE"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorityList);
    }
}
