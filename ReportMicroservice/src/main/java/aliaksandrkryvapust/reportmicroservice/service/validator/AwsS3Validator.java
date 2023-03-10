package aliaksandrkryvapust.reportmicroservice.service.validator;

import aliaksandrkryvapust.reportmicroservice.service.validator.api.IAwsS3Validator;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwsS3Validator implements IAwsS3Validator {
    private final AmazonS3 amazonS3;

    @Override
    public void validateFileKeyToStorage(String bucketName, String fileName) {
        if (!amazonS3.doesObjectExist(bucketName, fileName)){
            throw new IllegalStateException("There is no file with such name in the bucket" + fileName);
        }
    }
}
