package itacad.aliaksandrkryvapust.usermicroservice.service.validator;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserDetailsValidatorTest {
    @InjectMocks
    private UserDetailsValidator userDetailsValidator;
    @Mock
    private PasswordEncoder passwordEncoder;

    // preconditions
    final String email = "admin@tenderflex.com";
    final String password = "kdrL556D";

    @Test
    void validateLogin() {
        // preconditions
        final UserDtoLogin user = getPreparedUserDtoLogin();
        final UserDetails userDetails = getPreparedUserDetails();
        final String messageExpected = "User login or password is incorrect or user is not activated";

        //test
        Exception actualException = assertThrows(BadCredentialsException.class,
                () -> userDetailsValidator.validateLogin(user, userDetails));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }

    UserDetails getPreparedUserDetails() {
        Set<GrantedAuthority> authorityList = new HashSet<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + EUserRole.USER.name()));
        return new org.springframework.security.core.userdetails.User(email, password, true, true,
                true, true, authorityList);
    }

    UserDtoLogin getPreparedUserDtoLogin() {
        return UserDtoLogin.builder()
                .email(email)
                .password(password)
                .build();
    }
}