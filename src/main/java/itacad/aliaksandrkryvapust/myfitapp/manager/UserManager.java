package itacad.aliaksandrkryvapust.myfitapp.manager;

import itacad.aliaksandrkryvapust.myfitapp.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IUserManager;
import itacad.aliaksandrkryvapust.myfitapp.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserManager implements IUserManager {
    private final JwtUserDetailsService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserManager(JwtUserDetailsService userService, UserMapper userMapper,
                       AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDtoOutput login(UserDtoLogin userDtoLogin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDtoLogin.getUsername(), userDtoLogin.getPassword()));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = userService.loadUserByUsername(userDtoLogin.getUsername());
            String token = jwtTokenUtil.generateToken(userDetails);
            return this.userMapper.loginOutputMapping(userDetails, token);
        } else {
            return null;
        }
    }
//    @Override
//    public UserDtoOutput save(UserDtoInput userDtoInput) {
//        User user = this.userService.save(userMapper.inputMapping(userDtoInput));
//        return userMapper.outputCrudMapping(user);
//    }
//
//    @Override
//    public UserDtoOutput getUserByLogin(String login) {
//        return this.userService.
//    }
//
//    @Override
//    public List<MenuDtoCrudOutput> get() {
//        return this.userService.get().stream().map(userMapper::outputCrudMapping).collect(Collectors.toList());
//    }
//
//    @Override
//    public MenuDtoCrudOutput get(Long id) {
//        User menu = this.userService.get(id);
//        return userMapper.outputCrudMapping(menu);
//    }
//
//    @Override
//    public void delete(String login) {
//        this.userService.delete(login);
//    }
//
//    @Override
//    public UserDtoOutput update(UserDtoInput dtoInput, String login) {
//        User menu = this.userService.update(userMapper.inputMapping(menuDtoInput), id, version);
//        return userMapper.outputCrudMapping(menu);
//    }
}
