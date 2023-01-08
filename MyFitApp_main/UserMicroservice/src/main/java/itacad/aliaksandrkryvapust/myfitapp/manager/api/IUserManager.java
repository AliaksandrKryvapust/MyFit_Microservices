package itacad.aliaksandrkryvapust.myfitapp.manager.api;


import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserLoginDtoOutput;

import javax.servlet.http.HttpServletRequest;

public interface IUserManager extends IManager<UserDtoOutput, UserDtoInput>, IManagerUpdate<UserDtoOutput, UserDtoInput> {
    UserLoginDtoOutput login(UserDtoLogin userDtoLogin);
    UserLoginDtoOutput saveUser(UserDtoRegistration user, HttpServletRequest request);
    UserDtoOutput getUser(String email);
}
