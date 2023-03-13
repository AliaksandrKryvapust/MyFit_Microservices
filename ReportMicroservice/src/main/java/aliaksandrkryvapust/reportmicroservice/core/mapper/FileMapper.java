package aliaksandrkryvapust.reportmicroservice.core.mapper;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.FileDtoOutput;
import aliaksandrkryvapust.reportmicroservice.repository.entity.File;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FileMapper {

    public FileDtoOutput outputMapping(File file) {
        return FileDtoOutput.builder()
                .fileType(file.getFileType().name())
                .contentType(file.getContentType())
                .fileName(file.getFileName())
                .url(file.getUrl())
                .build();
    }
}
