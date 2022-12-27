package itacad.aliaksandrkryvapust.myfitapp.manager;

import itacad.aliaksandrkryvapust.myfitapp.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IUserManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IUserService;
import itacad.aliaksandrkryvapust.myfitapp.service.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
    public UserLoginDtoOutput login(UserDtoLogin userDtoLogin) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDtoLogin.getMail());
        if (!encoder.matches(userDtoLogin.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("User login or password is incorrect");
        }
        String token = jwtTokenUtil.generateToken(userDetails);
        return this.userMapper.loginOutputMapping(userDetails, token);
    }

    //    public UserLoginDtoOutput login(UserDtoLogin userDtoLogin) {
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
    public UserLoginDtoOutput saveUser(UserDtoRegistration userDtoRegistration) {
        User user = this.userService.save(userMapper.userInputMapping(userDtoRegistration));
        return userMapper.registerOutputMapping(user);
    }

    @Override
    public UserDtoOutput getUser(String email) {
        User user = this.userService.getUser(email);
        return this.userMapper.outputMapping(user);
    }

    @Override
    public UserDtoOutput save(UserDtoInput userDtoInput) {
        User user = this.userService.save(userMapper.inputMapping(userDtoInput));
        return userMapper.outputMapping(user);
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        return userMapper.outputPageMapping(this.userService.get(pageable));
    }

    @Override
    public UserDtoOutput get(UUID id) {
        User menu = this.userService.get(id);
        return userMapper.outputMapping(menu);
    }

    @Override
    public UserDtoOutput update(UserDtoInput dtoInput, UUID id, Long version) {
        User user = this.userService.update(userMapper.inputMapping(dtoInput), id, version);
        return userMapper.outputMapping(user);
    }
}
