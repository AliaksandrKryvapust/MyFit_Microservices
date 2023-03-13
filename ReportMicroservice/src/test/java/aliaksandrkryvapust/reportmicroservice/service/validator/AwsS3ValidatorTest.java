package aliaksandrkryvapust.reportmicroservice.service.validator;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AwsS3ValidatorTest {
    @InjectMocks
    private AwsS3Validator awsS3Validator;
    @Mock
    private AmazonS3 amazonS3;

    // preconditions
    final String fileName = "e6b8e68f-1457-451f-a4ff-5bb65212c8b6";
    final String bucketName = "javatests3111222";

    @Test
    void validateFileKeyToStorage() {
        // preconditions
        final String messageExpected = "There is no file with such name in the bucket" + fileName;
        Mockito.when(amazonS3.doesObjectExist(anyString(), anyString())).thenReturn(false);

        //test
        Exception actualException = assertThrows(IllegalStateException.class, () -> awsS3Validator
                .validateFileKeyToStorage(bucketName, fileName));

        // assert
        Assertions.assertEquals(messageExpected, actualException.getMessage());
    }
}