package aliaksandrkryvapust.reportmicroservice.controller.validation;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import org.springframework.core.convert.converter.Converter;

public class StringToTypeConverter implements Converter<String, EType> {

    @Override
    public EType convert(String source) {
        return EType.valueOf(source.toUpperCase());
    }
}
