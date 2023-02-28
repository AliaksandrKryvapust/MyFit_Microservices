package itacad.aliaksandrkryvapust.productmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ParamsMapper {

    public ParamsDto getParamsDto(HttpServletRequest request) {
        final LocalDate dateFrom = LocalDate.parse(request.getHeader("from"));
        final LocalDate dateTo = LocalDate.parse(request.getHeader("to"));
        final String userId = request.getHeader("id");
        return ParamsDto.builder()
                .from(dateFrom.atStartOfDay().toInstant(ZoneOffset.UTC))
                .to(dateTo.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC))
                .userId(userId)
                .build();
    }
}
