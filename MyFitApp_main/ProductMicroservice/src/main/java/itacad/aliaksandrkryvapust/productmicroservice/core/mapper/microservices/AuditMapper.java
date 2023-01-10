package itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.Type;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.UserDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuditMapper {
    private final UserMapper userMapper;

    @Autowired
    public AuditMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public AuditDto recordOutputMapping(Record record, MyUserDetails userDetails, String text) {
        UserDto userDto = userMapper.outputAuditMapping(userDetails);
        return AuditDto.builder()
                .id(String.valueOf(record.getId()))
                .user(userDto)
                .text(text)
                .type(Type.JOURNAL_FOOD)
                .build();
    }

    public AuditDto productOutputMapping(Product product, MyUserDetails userDetails, String text) {
        UserDto userDto = userMapper.outputAuditMapping(userDetails);
        return AuditDto.builder()
                .id(String.valueOf(product.getId()))
                .user(userDto)
                .text(text)
                .type(Type.PRODUCT)
                .build();
    }

    public AuditDto mealOutputMapping(Meal meal, MyUserDetails userDetails, String text) {
        UserDto userDto = userMapper.outputAuditMapping(userDetails);
        return AuditDto.builder()
                .id(String.valueOf(meal.getId()))
                .user(userDto)
                .text(text)
                .type(Type.RECIPE)
                .build();
    }

    public AuditDto profileOutputMapping(Profile profile, MyUserDetails userDetails, String text) {
        UserDto userDto = userMapper.outputAuditMapping(userDetails);
        return AuditDto.builder()
                .id(String.valueOf(profile.getId()))
                .user(userDto)
                .text(text)
                .type(Type.PROFILE)
                .build();
    }
}
