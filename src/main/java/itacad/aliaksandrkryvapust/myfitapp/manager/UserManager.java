package itacad.aliaksandrkryvapust.myfitapp.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

//@Component
//public class UserManager implements IUserManager {
//    private final IUserService userService;
//    private final UserMapper userMapper;
//
//    @Autowired
//    public UserManager(IUserService userService, UserMapper userMapper) {
//        this.userService = userService;
//        this.userMapper = userMapper;
//    }
//
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
//}
