package itacad.aliaksandrkryvapust.usermicroservice.controller.rest.security;

import itacad.aliaksandrkryvapust.usermicroservice.controller.rest.TokenValidationController;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import itacad.aliaksandrkryvapust.usermicroservice.service.TokenService;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenManager;
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

@WebMvcTest(controllers = TokenValidationController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc
class TokenValidationControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ITokenManager tokenManager;
    @MockBean
    SecurityContext securityContext;
    // Beans for JwtFilter
    @MockBean
    private JwtTokenUtil tokenUtil;
    @MockBean
    private JwtUserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "report@email", password = "report", roles = {"APP"})
    void validateToken() throws Exception {
        // preconditions

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/validateToken"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //test
        Mockito.verify(tokenManager, Mockito.times(1)).checkToken(any(HttpServletRequest.class));
    }
}