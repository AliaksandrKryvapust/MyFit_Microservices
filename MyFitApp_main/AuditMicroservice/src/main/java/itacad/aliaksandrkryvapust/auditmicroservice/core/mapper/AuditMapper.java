package itacad.aliaksandrkryvapust.auditmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.UserDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuditMapper {
    private final UserMapper userMapper;

    @Autowired
    public AuditMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Audit inputMapping(AuditDto auditDto) {
        User user = userMapper.inputMapping(auditDto.getUser());
        return Audit.builder().uuid(UUID.fromString(auditDto.getId()))
                .user(user)
                .text(auditDto.getText())
                .type(auditDto.getType())
                .build();
    }

    public AuditDto outputMapping(Audit audit) {
        UserDto userDto = userMapper.outputMapping(audit.getUser());
        return AuditDto.builder()
                .id(String.valueOf(audit.getUuid()))
                .user(userDto)
                .text(audit.getText())
                .type(audit.getType())
                .build();
    }

    public PageDtoOutput<AuditDto> outputPageMapping(Page<Audit> audits) {
        List<AuditDto> outputs = audits.getContent().stream().map(this::outputMapping).collect(Collectors.toList());
        return PageDtoOutput.<AuditDto>builder()
                .number(audits.getNumber() + 1)
                .size(audits.getSize())
                .totalPages(audits.getTotalPages())
                .totalElements(audits.getTotalElements())
                .first(audits.isFirst())
                .numberOfElements(audits.getNumberOfElements())
                .last(audits.isLast())
                .content(outputs)
                .build();
    }
}
