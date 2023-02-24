package itacad.aliaksandrkryvapust.usermicroservice.manager.api;


import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserRegistrationDtoOutput;

import javax.servlet.http.HttpServletRequest;

public interface IUserManager extends IManager<UserDtoOutput, UserDtoInput>, IManagerUpdate<UserDtoOutput, UserDtoInput> {
    UserLoginDtoOutput login(UserDtoLogin userDtoLogin);
    UserRegistrationDtoOutput saveUser(UserDtoRegistration user);
    UserDtoOutput getUserDto(String email);
}
