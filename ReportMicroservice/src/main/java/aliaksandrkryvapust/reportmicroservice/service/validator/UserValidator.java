package aliaksandrkryvapust.reportmicroservice.service.validator;

import aliaksandrkryvapust.reportmicroservice.repository.entity.User;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IUserValidator;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements IUserValidator {
    @Override
    public void validateEntity(User entityToSave) {
        checkId(entityToSave);
        checkEmail(entityToSave);
        checkUserRole(entityToSave);
    }

    private void checkId(User user) {
        if (user.getUserId() == null) {
            throw new IllegalArgumentException("User id is empty for user: " + user);
        }
    }

    private void checkUserRole(User user) {
        if (user.getRole() == null) {
            throw new IllegalArgumentException("user role is not valid for user:" + user);
        }
    }

    private void checkEmail(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("email is not valid for user:" + user);
        }
        char[] chars = user.getUsername().toCharArray();
        if (chars.length < 2 || chars.length > 50) {
            throw new IllegalArgumentException("email should contain from 2 to 50 letters for user:" + user);
        }
    }
}
