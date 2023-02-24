package itacad.aliaksandrkryvapust.usermicroservice.service.validator;

import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.service.validator.api.IUserValidator;
import org.springframework.stereotype.Component;

import javax.persistence.OptimisticLockException;

@Component
public class UserValidator implements IUserValidator {

    @Override
    public void validateEntity(User user) {
        checkAuxiliaryFields(user);
        checkUsername(user);
        checkPassword(user);
        checkEmail(user);
        checkUserRole(user);
        checkUserStatus(user);
    }

    @Override
    public void optimisticLockCheck(Long version, User currentEntity) {
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("user table update failed, version does not match update denied");
        }
    }

    private void checkAuxiliaryFields(User user) {
        if (user.getId() != null) {
            throw new IllegalStateException("User id should be empty for user: " + user);
        }
        if (user.getDtUpdate() != null) {
            throw new IllegalStateException("User version should be empty for user: " + user);
        }
        if (user.getDtCreate() != null) {
            throw new IllegalStateException("User creation date should be empty for user: " + user);
        }
    }

    private void checkUserStatus(User user) {
        if (user.getStatus() == null) {
            throw new IllegalArgumentException("user status is not valid for user:" + user);
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

    private void checkPassword(User user) {
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("password is not valid for user:" + user);
        }
        char[] chars = user.getPassword().toCharArray();
        if (chars.length < 2 || chars.length > 200) {
            throw new IllegalArgumentException("password should contain from 2 to 200 letters for user:" + user);
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
