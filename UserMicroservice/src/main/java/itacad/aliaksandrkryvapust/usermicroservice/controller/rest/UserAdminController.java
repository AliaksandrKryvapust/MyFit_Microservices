package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IUserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class UserAdminController {
    private final IUserManager userManager;

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput<UserDtoOutput>> getPage(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dtCreate").descending());
        PageDtoOutput<UserDtoOutput> dtoOutput = userManager.getDto(pageable);
        return ResponseEntity.ok(dtoOutput);
    }

    @GetMapping("/{id}")
    protected ResponseEntity<UserDtoOutput> get(@PathVariable UUID id) {
        UserDtoOutput dtoOutput = userManager.getDto(id);
        return ResponseEntity.ok(dtoOutput);
    }

    @PostMapping
    protected ResponseEntity<UserDtoOutput> post(@RequestBody @Valid UserDtoInput dtoInput) {
        UserDtoOutput dtoOutput = userManager.saveDto(dtoInput);
        return new ResponseEntity<>(dtoOutput, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/version/{version}")
    protected ResponseEntity<UserDtoOutput> put(@PathVariable UUID id, @PathVariable(name = "version") String version,
                                                @Valid @RequestBody UserDtoInput dtoInput) {
        UserDtoOutput dtoOutput = userManager.updateDto(dtoInput, id, Long.valueOf(version));
        return ResponseEntity.ok(dtoOutput);
    }
}
