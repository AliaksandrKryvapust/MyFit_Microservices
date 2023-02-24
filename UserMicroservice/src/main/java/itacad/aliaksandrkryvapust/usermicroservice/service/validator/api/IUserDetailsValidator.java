package itacad.aliaksandrkryvapust.usermicroservice.service.validator.api;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoLogin;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserDetailsValidator {
    void validateLogin(UserDtoLogin userDtoLogin, UserDetails userDetails);
}
