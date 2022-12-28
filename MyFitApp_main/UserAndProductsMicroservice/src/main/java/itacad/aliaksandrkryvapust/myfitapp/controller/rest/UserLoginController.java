package itacad.aliaksandrkryvapust.myfitapp.controller.rest;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/users")
public class UserLoginController {
    private final IUserManager userManager;

    @Autowired
    public UserLoginController(IUserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping("/me")
    protected ResponseEntity<UserDtoOutput> getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        return ResponseEntity.ok(this.userManager.getUser(username));
    }

    @PostMapping("/login")
    protected ResponseEntity<UserLoginDtoOutput> login(@RequestBody @Valid UserDtoLogin dtoLogin) {
        UserLoginDtoOutput userLoginDtoOutput = userManager.login(dtoLogin);
        return ResponseEntity.ok(userLoginDtoOutput);
    }

    @PostMapping("/registration")
    protected ResponseEntity<UserLoginDtoOutput> registration(@RequestBody @Valid UserDtoRegistration dtoInput,
                                                              HttpServletRequest request) {
        return new ResponseEntity<>(this.userManager.saveUser(dtoInput, request), HttpStatus.CREATED);
    }
}
