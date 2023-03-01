package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProfileDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProfileDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProfileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final IProfileManager profileManager;

    @GetMapping("/{id}")
    protected ResponseEntity<ProfileDtoOutput> get(@PathVariable UUID id) {
        ProfileDtoOutput dtoOutput = profileManager.getDto(id);
        return ResponseEntity.ok(dtoOutput);
    }

    @PostMapping
    protected ResponseEntity<ProfileDtoOutput> post(@RequestBody @Valid ProfileDtoInput dtoInput) {
        ProfileDtoOutput dtoOutput = profileManager.saveDto(dtoInput);
        return new ResponseEntity<>(dtoOutput, HttpStatus.CREATED);
    }
}
