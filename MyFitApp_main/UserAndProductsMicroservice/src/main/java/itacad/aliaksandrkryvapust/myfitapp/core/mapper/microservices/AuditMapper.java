package itacad.aliaksandrkryvapust.myfitapp.core.mapper.microservices;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.microservices.Type;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
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


    public AuditDto userOutputMapping(User user, String text) {
        UserDtoOutput userDto = userMapper.outputMapping(user);
        return AuditDto.builder()
                .id(String.valueOf(user.getId()))
                .user(userDto)
                .text(text)
                .type(Type.USER)
                .build();
    }
}
