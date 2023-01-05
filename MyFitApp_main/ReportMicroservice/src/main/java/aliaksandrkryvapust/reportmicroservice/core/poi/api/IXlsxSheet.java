package aliaksandrkryvapust.reportmicroservice.core.poi.api;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IXlsxSheet {
    String value();
}
