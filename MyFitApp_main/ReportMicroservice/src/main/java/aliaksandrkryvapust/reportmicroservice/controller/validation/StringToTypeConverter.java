package aliaksandrkryvapust.reportmicroservice.controller.validation;

import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import org.springframework.core.convert.converter.Converter;

public class StringToTypeConverter implements Converter<String, Type> {

    @Override
    public Type convert(String source) {
        return Type.valueOf(source.toUpperCase());
    }
}
