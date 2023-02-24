package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserRegistrationDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.ITokenManager;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.IUserManager;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserLoginController {
    private final IUserManager userManager;
    private final ITokenManager tokenManager;
    private final JwtUserDetailsService userDetailsService;

    @GetMapping
    protected ResponseEntity<UserDtoOutput> getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        UserDtoOutput dtoOutput = userManager.getUserDto(username);
        return ResponseEntity.ok(dtoOutput);
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        userDetailsService.logout(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    protected ResponseEntity<UserLoginDtoOutput> login(@RequestBody @Valid UserDtoLogin dtoLogin) {
        UserLoginDtoOutput userLoginDtoOutput = userDetailsService.login(dtoLogin);
        HttpHeaders jwtCookie = createJwtCookie(userLoginDtoOutput.getToken());
        return ResponseEntity.ok().headers(jwtCookie).body(userLoginDtoOutput);
    }

    @PostMapping("/registration")
    protected ResponseEntity<UserRegistrationDtoOutput> registration(@RequestBody @Valid UserDtoRegistration dtoInput) {
        UserRegistrationDtoOutput userLoginDtoOutput = userManager.saveUser(dtoInput);
        return new ResponseEntity<>(userLoginDtoOutput, HttpStatus.CREATED);
    }

    @GetMapping(value = "/registration/confirm", params = "token")
    protected ResponseEntity<Object> registrationConfirmation(@RequestParam String token) {
        tokenManager.validateToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/registration/confirm/token", params = "token")
    protected ResponseEntity<Object> resendToken(@RequestParam String token) {
        tokenManager.resendToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private HttpHeaders createJwtCookie(String token) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpCookie responseCookie = userDetailsService.createJwtCookie(token);
        responseHeaders.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return responseHeaders;
    }
}
