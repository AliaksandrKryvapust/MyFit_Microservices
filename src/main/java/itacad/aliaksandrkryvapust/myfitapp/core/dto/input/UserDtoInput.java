package itacad.aliaksandrkryvapust.myfitapp.core.dto.input;

import itacad.aliaksandrkryvapust.myfitapp.controller.validator.api.IValidEmail;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


//@Builder
//@Data
//@Jacksonized
//public class UserDtoInput {
//    @NotNull(message = "username cannot be null")
//    @Size(min = 2, max = 50,message = "username should contain from 2 to 50 letters")
//    private final String username;
//    @NotNull(message = "pizza info cannot be null")
//    @Size(min = 2, max = 200,message = "username should contain from 2 to 200 letters")
//    private final String password;
//    @NotNull(message = "pizza info cannot be null")
//    @NotEmpty
//    @IValidEmail
//    private final String email;
//    private final Integer height;
//    private final Double weight;
//    private final Integer age;
//}
