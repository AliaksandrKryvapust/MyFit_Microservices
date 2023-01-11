package itacad.aliaksandrkryvapust.auditmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.TokenValidationDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.UserDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.security.UserPrincipal;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMapper {

    public UserPrincipal inputValidationMapping(TokenValidationDto dto) {
        return UserPrincipal.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .authenticated(dto.getAuthenticated())
                .role(dto.getRole())
                .build();
    }

    public User inputMapping(UserDto userDto) {
        return User.builder().id(userDto.getUuid())
                .username(userDto.getNick())
                .email(userDto.getMail())
                .role(userDto.getRole())
                .status(userDto.getStatus())
                .dtCreate(userDto.getDtCreate())
                .dtUpdate(userDto.getDtUpdate())
                .build();
    }

    public UserDtoOutput outputMapping(User user) {
        return UserDtoOutput.builder()
                .uuid(user.getId())
                .dtCreate(user.getDtCreate())
                .dtUpdate(user.getDtUpdate())
                .mail(user.getEmail())
                .nick(user.getUsername())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public PageDtoOutput<UserDtoOutput> outputPageMapping(Page<User> record) {
        List<UserDtoOutput> outputs = record.getContent().stream().map(this::outputMapping).collect(Collectors.toList());
        return PageDtoOutput.<UserDtoOutput>builder()
                .number(record.getNumber() + 1)
                .size(record.getSize())
                .totalPages(record.getTotalPages())
                .totalElements(record.getTotalElements())
                .first(record.isFirst())
                .numberOfElements(record.getNumberOfElements())
                .last(record.isLast())
                .content(outputs)
                .build();
    }
}
