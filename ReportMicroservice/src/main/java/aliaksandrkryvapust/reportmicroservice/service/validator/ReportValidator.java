package aliaksandrkryvapust.reportmicroservice.service.validator;

import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IParamsValidator;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IReportValidator;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportValidator implements IReportValidator {
    private final IUserValidator userValidator;
    private final IParamsValidator paramsValidator;

    @Override
    public void validateEntity(Report report) {
        checkAuxiliaryFields(report);
        checkStatus(report);
        checkType(report);
        checkDescription(report);
        checkUser(report);
        checkParams(report);
    }

    private void checkAuxiliaryFields(Report report) {
        if (report.getId() != null) {
            throw new IllegalStateException("report id should be empty for report: " + report);
        }
        if (report.getDtUpdate() != null) {
            throw new IllegalStateException("report version should be empty for report: " + report);
        }
        if (report.getDtCreate() != null) {
            throw new IllegalStateException("report creation date should be empty for report: " + report);
        }
    }

    private void checkStatus(Report report) {
        if (report.getStatus() == null) {
            throw new IllegalArgumentException("status is not valid for report:" + report);
        }
    }

    private void checkType(Report report) {
        if (report.getType() == null) {
            throw new IllegalArgumentException("type is not valid for report:" + report);
        }
    }

    private void checkDescription(Report report) {
        if (report.getDescription() == null || report.getDescription().isBlank()) {
            throw new IllegalArgumentException("description is not valid for report:" + report);
        }
        char[] chars = report.getDescription().toCharArray();
        if (chars.length < 2 || chars.length > 50) {
            throw new IllegalArgumentException("description should contain from 2 to 50 letters for report:" + report);
        }
    }

    private void checkUser(Report report) {
        if (report.getUser() == null) {
            throw new IllegalArgumentException("user is not valid for report:" + report);
        }
        userValidator.validateEntity(report.getUser());
    }

    private void checkParams(Report report) {
        if (report.getParams() == null) {
            throw new IllegalArgumentException("params is not valid for report:" + report);
        }
        paramsValidator.validateEntity(report.getParams());
    }
}
