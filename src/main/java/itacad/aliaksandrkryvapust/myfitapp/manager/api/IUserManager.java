package itacad.aliaksandrkryvapust.myfitapp.manager.api;


import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;

public interface IUserManager {
    UserDtoOutput login(UserDtoLogin userDtoLogin);
    UserDtoOutput saveUser(UserDtoInput user);
//    void delete(String login);
}
