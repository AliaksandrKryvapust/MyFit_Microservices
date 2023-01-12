package itacad.aliaksandrkryvapust.usermicroservice.controller.validator;

import itacad.aliaksandrkryvapust.usermicroservice.controller.validator.api.IValidEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<IValidEnum, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(IValidEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString());
    }
}
