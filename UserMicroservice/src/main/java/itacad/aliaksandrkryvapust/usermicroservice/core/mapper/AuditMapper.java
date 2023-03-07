package itacad.aliaksandrkryvapust.usermicroservice.core.mapper;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.Type;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.UserDto;
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
        UserDto userDto = userMapper.auditMapping(user);
        return AuditDto.builder()
                .id(String.valueOf(user.getId()))
                .user(userDto)
                .text(text)
                .type(Type.USER.name())
                .build();
    }
}
