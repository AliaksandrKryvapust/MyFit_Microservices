package itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
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

    public AuditDto recordOutputMapping(Record record, User user, String text) {
        UserDtoOutput userDto = userMapper.outputMapping(user);
        return AuditDto.builder()
                .id(String.valueOf(record.getId()))
                .user(userDto)
                .text(text)
                .type(Type.JOURNAL_FOOD)
                .build();
    }

    public AuditDto productOutputMapping(Product product, User user, String text) {
        UserDtoOutput userDto = userMapper.outputMapping(user);
        return AuditDto.builder()
                .id(String.valueOf(product.getId()))
                .user(userDto)
                .text(text)
                .type(Type.PRODUCT)
                .build();
    }

    public AuditDto mealOutputMapping(Meal meal, User user, String text) {
        UserDtoOutput userDto = userMapper.outputMapping(user);
        return AuditDto.builder()
                .id(String.valueOf(meal.getId()))
                .user(userDto)
                .text(text)
                .type(Type.RECIPE)
                .build();
    }
}
