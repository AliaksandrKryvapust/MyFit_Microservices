package itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.EType;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.UserDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuditMapper {
    private final UserMapper userMapper;

    public AuditDto recordOutputMapping(Record record, MyUserDetails userDetails, String text) {
        UserDto userDto = userMapper.outputAuditMapping(userDetails);
        return AuditDto.builder()
                .id(String.valueOf(record.getId()))
                .user(userDto)
                .text(text)
                .type(EType.JOURNAL_FOOD.name())
                .build();
    }

    public AuditDto productOutputMapping(Product product, MyUserDetails userDetails, String text) {
        UserDto userDto = userMapper.outputAuditMapping(userDetails);
        return AuditDto.builder()
                .id(String.valueOf(product.getId()))
                .user(userDto)
                .text(text)
                .type(EType.PRODUCT.name())
                .build();
    }

    public AuditDto mealOutputMapping(Meal meal, MyUserDetails userDetails, String text) {
        UserDto userDto = userMapper.outputAuditMapping(userDetails);
        return AuditDto.builder()
                .id(String.valueOf(meal.getId()))
                .user(userDto)
                .text(text)
                .type(EType.RECIPE.name())
                .build();
    }

    public AuditDto profileOutputMapping(Profile profile, MyUserDetails userDetails, String text) {
        UserDto userDto = userMapper.outputAuditMapping(userDetails);
        return AuditDto.builder()
                .id(String.valueOf(profile.getId()))
                .user(userDto)
                .text(text)
                .type(EType.PROFILE.name())
                .build();
    }
}
