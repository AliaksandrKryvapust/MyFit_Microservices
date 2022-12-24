package itacad.aliaksandrkryvapust.myfitapp.controller.rest;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@Validated
@RequestMapping("/api/public")
public class UserLoginController {
    private final IUserManager userManager;

    @Autowired
    public UserLoginController(IUserManager userManager) {
        this.userManager = userManager;
    }

    @PostMapping("/login")
    protected ResponseEntity<UserDtoOutput> login(@RequestBody @Valid UserDtoLogin dtoLogin) {
        UserDtoOutput userDtoOutput = userManager.login(dtoLogin);
        return new ResponseEntity<>(userDtoOutput, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    protected ResponseEntity<UserDtoOutput> registration(@RequestBody @Valid UserDtoInput dtoInput) {
        return new ResponseEntity<>(this.userManager.saveUser(dtoInput), HttpStatus.CREATED);
    }
}
