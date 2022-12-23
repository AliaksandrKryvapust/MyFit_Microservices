package itacad.aliaksandrkryvapust.myfitapp.manager.api;


import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;

public interface IUserManager {
    UserDtoOutput login(UserDtoLogin userDtoLogin);
//
//    UserDtoOutput update(UserDtoInput user, String login);
//    void delete(String login);
}
