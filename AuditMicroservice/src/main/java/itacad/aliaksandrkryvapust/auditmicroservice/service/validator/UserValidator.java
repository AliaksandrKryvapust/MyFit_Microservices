package itacad.aliaksandrkryvapust.auditmicroservice.service.validator;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.auditmicroservice.service.validator.api.IUserValidator;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements IUserValidator {
    @Override
    public void validateEntity(User entityToSave) {
        checkId(entityToSave);
        checkUsername(entityToSave);
        checkEmail(entityToSave);
        checkUserRole(entityToSave);
    }


    private void checkId(User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("User id is empty for user: " + user);
        }
    }

    private void checkUserRole(User user) {
        if (user.getRole() == null) {
            throw new IllegalArgumentException("user role is not valid for user:" + user);
        }
    }

    private void checkEmail(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("email is not valid for user:" + user);
        }
        char[] chars = user.getEmail().toCharArray();
        if (chars.length < 2 || chars.length > 50) {
            throw new IllegalArgumentException("email should contain from 2 to 50 letters for user:" + user);
        }
    }

    private void checkUsername(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is not valid for user:" + user);
        }
        char[] chars = user.getUsername().toCharArray();
        if (chars.length < 2 || chars.length > 50) {
            throw new IllegalArgumentException("username should contain from 2 to 50 letters for user:" + user);
        }
    }
}
