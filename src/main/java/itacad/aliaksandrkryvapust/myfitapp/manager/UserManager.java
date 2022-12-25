package itacad.aliaksandrkryvapust.myfitapp.manager;

import itacad.aliaksandrkryvapust.myfitapp.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IUserManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IUserService;
import itacad.aliaksandrkryvapust.myfitapp.service.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserManager implements IUserManager {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final IUserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    private final PasswordEncoder encoder;

    @Autowired
    public UserManager(JwtUserDetailsService jwtUserDetailsService, IUserService userService, UserMapper userMapper,
                       AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, PasswordEncoder encoder) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.encoder = encoder;
    }

    @Override
    public UserDtoOutput login(UserDtoLogin userDtoLogin) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDtoLogin.getMail());
        if (!encoder.matches(userDtoLogin.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("User login or password is incorrect");
        }
        String token = jwtTokenUtil.generateToken(userDetails);
        return this.userMapper.loginOutputMapping(userDetails, token);
    }

    //    public UserDtoOutput login(UserDtoLogin userDtoLogin) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userDtoLogin.getUsername(), userDtoLogin.getPassword()));
//        if (authentication.isAuthenticated()) {
//            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDtoLogin.getUsername());
//            String token = jwtTokenUtil.generateToken(userDetails);
//            return this.userMapper.loginOutputMapping(userDetails, token);
//        } else {
//            return null;
//        }
//    }
    @Override
    public UserDtoOutput saveUser(UserDtoInput userDtoInput) {
        User user = this.userService.save(userMapper.userInputMapping(userDtoInput));
        return userMapper.outputMapping(user);
    }

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
