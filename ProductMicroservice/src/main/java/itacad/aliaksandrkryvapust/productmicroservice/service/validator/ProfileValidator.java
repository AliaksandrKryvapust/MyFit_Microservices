package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IProfileValidator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.OptimisticLockException;
import java.time.LocalDate;

@Component
public class ProfileValidator implements IProfileValidator {

    @Override
    public void checkCredentials(Profile currentEntity) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentEntity.getUser().getUserId().equals(userDetails.getId())) {
            throw new BadCredentialsException("It`s forbidden to modify not private data");
        }
    }

    @Override
    public void validateEntity(Profile profile) {
        checkAuxiliaryFields(profile);
        checkHeight(profile);
        checkWeight(profile);
        checkBirthday(profile);
        checkTarget(profile);
        checkActivityType(profile);
        checkSex(profile);
    }

    @Override
    public void optimisticLockCheck(Long version, Profile currentEntity) {
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("profile table update failed, version does not match update denied");
        }
    }


    private void checkAuxiliaryFields(Profile profile) {
        if (profile.getId() != null) {
            throw new IllegalStateException("profile id should be empty for profile: " + profile);
        }
        if (profile.getDtUpdate() != null) {
            throw new IllegalStateException("profile version should be empty for profile: " + profile);
        }
        if (profile.getDtCreate() != null) {
            throw new IllegalStateException("profile creation date should be empty for profile: " + profile);
        }
    }

    private void checkSex(Profile profile) {
        if (profile.getSex() == null) {
            throw new IllegalArgumentException("sex is not valid for profile:" + profile);
        }
    }

    private void checkActivityType(Profile profile) {
        if (profile.getActivityType() == null) {
            throw new IllegalArgumentException("activity type is not valid for profile:" + profile);
        }
    }

    private void checkTarget(Profile profile) {
        if (profile.getTarget() == null || profile.getTarget() <= 0) {
            throw new IllegalArgumentException("target is not valid for profile:" + profile);
        }
    }

    private void checkBirthday(Profile profile) {
        if (profile.getDtBirthday() == null || profile.getDtBirthday().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("birthday date is not valid for profile:" + profile);
        }
    }

    private void checkWeight(Profile profile) {
        if (profile.getWeight() == null || profile.getWeight() <= 0) {
            throw new IllegalArgumentException("weight is not valid for profile:" + profile);
        }
    }

    private void checkHeight(Profile profile) {
        if (profile.getHeight() == null || profile.getHeight() <= 0) {
            throw new IllegalArgumentException("height is not valid for profile:" + profile);
        }
    }
}
