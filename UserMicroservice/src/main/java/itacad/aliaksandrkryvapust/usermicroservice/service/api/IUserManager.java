package itacad.aliaksandrkryvapust.usermicroservice.service.api;


import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserRegistrationDtoOutput;

public interface IUserManager extends IManager<UserDtoOutput, UserDtoInput>, IManagerUpdate<UserDtoOutput, UserDtoInput> {
    UserRegistrationDtoOutput saveUser(UserDtoRegistration user);
}
