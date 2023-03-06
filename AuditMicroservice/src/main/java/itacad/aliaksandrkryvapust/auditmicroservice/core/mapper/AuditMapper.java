package itacad.aliaksandrkryvapust.auditmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.AuditDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EType;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuditMapper {
    private final UserMapper userMapper;

    public Audit inputMapping(AuditDto auditDto) {
        User user = userMapper.inputMapping(auditDto.getUser());
        return Audit.builder()
                .actionId(UUID.fromString(auditDto.getId()))
                .user(user)
                .text(auditDto.getText())
                .type(EType.valueOf(auditDto.getType()))
                .build();
    }

    public AuditDtoOutput outputMapping(Audit audit) {
        UserDtoOutput userDto = userMapper.outputMapping(audit.getUser());
        return AuditDtoOutput.builder()
                .id(String.valueOf(audit.getActionId()))
                .actionId(audit.getActionId().toString())
                .user(userDto)
                .text(audit.getText())
                .type(audit.getType().name())
                .build();
    }

    public List<AuditDtoOutput> outputListMapping(List<Audit> audit) {
        return audit.stream()
                .map(this::outputMapping)
                .collect(Collectors.toList());
    }

    public PageDtoOutput<AuditDtoOutput> outputPageMapping(Page<Audit> audits) {
        List<AuditDtoOutput> outputs = audits.getContent().stream()
                .map(this::outputMapping)
                .collect(Collectors.toList());
        return PageDtoOutput.<AuditDtoOutput>builder()
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
