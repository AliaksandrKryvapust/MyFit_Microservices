package itacad.aliaksandrkryvapust.usermicroservice.controller.validator;

import itacad.aliaksandrkryvapust.usermicroservice.controller.validator.api.IValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<IValidEmail, String> {
    private static final String EMAIL_PATTERN = "^[_A-Za-z\\d-+]+(.[_A-Za-z\\d-]+)*@" +
                "[A-Za-z\\d-]+(.[A-Za-z\\d]+)*(.[A-Za-z]{2,})$";
        @Override
        public void initialize(IValidEmail constraintAnnotation) {
        }
        @Override
        public boolean isValid(String email, ConstraintValidatorContext context){
            return (validateEmail(email));
        }
        private boolean validateEmail(String email) {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
}
