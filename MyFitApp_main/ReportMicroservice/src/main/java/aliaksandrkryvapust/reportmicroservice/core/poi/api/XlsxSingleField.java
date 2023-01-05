package aliaksandrkryvapust.reportmicroservice.core.poi.api;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XlsxSingleField {
    int columnIndex();
}
