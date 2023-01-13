package itacad.aliaksandrkryvapust.usermicroservice.service.api;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.TokenValidationDto;

import javax.servlet.http.HttpServletRequest;

public interface ITokenValidationService {
    TokenValidationDto validateToken(HttpServletRequest request);
}
