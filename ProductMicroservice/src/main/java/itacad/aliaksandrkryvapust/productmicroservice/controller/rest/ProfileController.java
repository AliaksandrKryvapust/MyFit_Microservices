package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProfileDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProfileDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IProfileManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final IProfileManager profileManager;

    public ProfileController(IProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @GetMapping("/{id}")
    protected ResponseEntity<ProfileDtoOutput> get(@PathVariable UUID id) {
        return ResponseEntity.ok(profileManager.get(id));
    }

    @PostMapping
    protected ResponseEntity<ProfileDtoOutput> post(@RequestBody @Valid ProfileDtoInput dtoInput) {
        return new ResponseEntity<>(this.profileManager.save(dtoInput), HttpStatus.CREATED);
    }
}
