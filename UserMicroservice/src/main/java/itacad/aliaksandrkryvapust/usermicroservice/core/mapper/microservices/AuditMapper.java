package itacad.aliaksandrkryvapust.usermicroservice.core.mapper.microservices;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.Type;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuditMapper {
    private final UserMapper userMapper;

    public AuditDto userOutputMapping(User user, String text) {
        UserDtoOutput userDto = userMapper.outputMapping(user);
        return AuditDto.builder()
                .id(String.valueOf(user.getId()))
                .user(userDto)
                .text(text)
                .type(Type.USER.name())
                .build();
    }
}
