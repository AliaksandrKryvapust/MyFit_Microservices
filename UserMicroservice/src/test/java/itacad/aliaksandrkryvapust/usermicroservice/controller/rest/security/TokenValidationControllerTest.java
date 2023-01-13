package itacad.aliaksandrkryvapust.usermicroservice.controller.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.ITokenManager;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.IUserManager;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import itacad.aliaksandrkryvapust.usermicroservice.service.TokenValidationService;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

@WebMvcTest(controllers = TokenValidationController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc
class TokenValidationControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TokenValidationService tokenValidationService;
    @MockBean
    SecurityContext securityContext;
    // Beans for JwtFilter
    @MockBean
    private JwtTokenUtil tokenUtil;
    @MockBean
    private ITokenManager tokenManager;
    @MockBean
    private JwtUserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "report@email", password = "report", roles = {"APP"})
    void validateToken() throws Exception {
        // preconditions
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/validateToken"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //test
        Mockito.verify(tokenValidationService, Mockito.times(1)).validateToken(any(HttpServletRequest.class));
    }
}