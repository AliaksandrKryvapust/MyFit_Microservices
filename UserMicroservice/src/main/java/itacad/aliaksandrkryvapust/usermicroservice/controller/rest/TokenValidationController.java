package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/validateToken")
public class TokenValidationController {
    private final ITokenManager tokenValidationService;

    @GetMapping
    public ResponseEntity<TokenValidationDto> validateToken(HttpServletRequest request) {
        TokenValidationDto validationDto = tokenValidationService.checkToken(request);
        return ResponseEntity.ok(validationDto);
    }
}
