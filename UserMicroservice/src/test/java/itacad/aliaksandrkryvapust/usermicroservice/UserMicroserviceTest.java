package itacad.aliaksandrkryvapust.usermicroservice;

import itacad.aliaksandrkryvapust.usermicroservice.controller.rest.UserLoginController;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = UserLoginController.class)
@AutoConfigureMockMvc
class UserMicroserviceTest {



}
