package itacad.aliaksandrkryvapust.usermicroservice.controller.validator.api;

import itacad.aliaksandrkryvapust.usermicroservice.controller.validator.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface IValidEmail {
    String message() default "Email is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
