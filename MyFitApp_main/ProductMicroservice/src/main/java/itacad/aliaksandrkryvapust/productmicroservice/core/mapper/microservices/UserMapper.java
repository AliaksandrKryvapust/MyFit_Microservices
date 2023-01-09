package itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.TokenValidationDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.UserDto;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMapper {
    public User inputMapping(TokenValidationDto dto){
        return User.builder()
                .user_id(dto.getId())
                .username(dto.getUsername())
                .version(dto.getDtUpdate()).build();
    }

    public UserDto outputAuditMapping(UserDetails userDetails){
        return UserDto.builder()
                .mail(userDetails.getUsername())
                .role(EUserRole.valueOf(userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority()))
                .build();
    }

    public UserDtoOutput outputMapping(User user){
        return UserDtoOutput.builder()
                .user_id(user.getUser_id())
                .username(user.getUsername())
                .version(user.getVersion()).build();
    }
}
