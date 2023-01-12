package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/users")
public class UserController {
    private final IUserManager userManager;

    @Autowired
    public UserController(IUserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput> getPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userManager.get(pageable));
    }

    @GetMapping("/{id}")
    protected ResponseEntity<UserDtoOutput> get(@PathVariable UUID id) {
        return ResponseEntity.ok(userManager.get(id));
    }

    @PostMapping
    protected ResponseEntity<UserDtoOutput> post(@RequestBody @Valid UserDtoInput dtoInput, HttpServletRequest request) {
        return new ResponseEntity<>(this.userManager.save(dtoInput, request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/dt_update/{version}")
    protected ResponseEntity<UserDtoOutput> put(@PathVariable UUID id, @PathVariable(name = "version") String version,
                                                @Valid @RequestBody UserDtoInput dtoInput, HttpServletRequest request) {
        return ResponseEntity.ok(this.userManager.update(dtoInput, id, Long.valueOf(version), request));
    }

}