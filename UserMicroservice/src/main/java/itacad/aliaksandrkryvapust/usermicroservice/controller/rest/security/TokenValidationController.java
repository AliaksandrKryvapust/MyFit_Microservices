package itacad.aliaksandrkryvapust.usermicroservice.controller.rest.security;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.usermicroservice.service.TokenValidationService;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/validateToken")
public class TokenValidationController {
    private final ITokenValidationService tokenValidationService;

    public TokenValidationController(TokenValidationService tokenValidationService) {
        this.tokenValidationService = tokenValidationService;
    }

    @GetMapping
    public ResponseEntity<TokenValidationDto> validateToken(HttpServletRequest request){
        return ResponseEntity.ok(tokenValidationService.validateToken(request));
    }
}
