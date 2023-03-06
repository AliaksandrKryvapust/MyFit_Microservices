package itacad.aliaksandrkryvapust.auditmicroservice.service.validator;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.service.validator.api.IAuditValidator;
import itacad.aliaksandrkryvapust.auditmicroservice.service.validator.api.IUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditValidator implements IAuditValidator {
    private final IUserValidator userValidator;

    @Override
    public void validateEntity(Audit entityToSave) {
        checkAuxiliaryFields(entityToSave);
        checkActionId(entityToSave);
        checkUser(entityToSave);
        checkText(entityToSave);
        checkType(entityToSave);
    }

    private void checkAuxiliaryFields(Audit entityToSave) {
        if (entityToSave.getId() != null) {
            throw new IllegalStateException("Audit id should be empty for audit: " + entityToSave);
        }
        if (entityToSave.getDtCreate() != null) {
            throw new IllegalStateException("Audit creation date should be empty for audit: " + entityToSave);
        }
    }

    private void checkType(Audit audit) {
        if (audit.getType() == null) {
            throw new IllegalArgumentException("type is not valid for audit:" + audit);
        }
    }

    private void checkText(Audit audit) {
        if (audit.getText() == null || audit.getText().isBlank()) {
            throw new IllegalArgumentException("text is not valid for audit:" + audit);
        }
        char[] chars = audit.getText().toCharArray();
        if (chars.length < 2 || chars.length > 50) {
            throw new IllegalArgumentException("text should contain from 2 to 50 letters for audit:" + audit);
        }
    }

    private void checkUser(Audit audit) {
        if (audit.getUser() == null) {
            throw new IllegalArgumentException("user is not valid for audit:" + audit);
        }
        userValidator.validateEntity(audit.getUser());
    }

    private void checkActionId(Audit audit) {
        if (audit.getActionId() == null) {
            throw new IllegalArgumentException("action id is not valid for audit:" + audit);
        }
    }
}
