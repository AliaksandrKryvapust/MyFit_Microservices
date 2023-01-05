package aliaksandrkryvapust.reportmicroservice.core.poi.api;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XlsxCompositeField {
    int from();
    int to();
}
