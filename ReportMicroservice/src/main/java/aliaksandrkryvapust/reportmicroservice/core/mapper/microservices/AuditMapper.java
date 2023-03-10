package aliaksandrkryvapust.reportmicroservice.core.mapper.microservices;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.AuditDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.UserDto;
import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuditMapper {
    private final UserMapper userMapper;

    public AuditDto reportOutputMapping(Report report, MyUserDetails userDetails, String text) {
        UserDto userDto = userMapper.outputAuditMapping(userDetails);
        return AuditDto.builder()
                .id(String.valueOf(report.getId()))
                .user(userDto)
                .text(text)
                .type(EType.REPORT.name())
                .build();
    }
}
