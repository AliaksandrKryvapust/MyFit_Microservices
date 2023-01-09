package itacad.aliaksandrkryvapust.productmicroservice.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.RecordDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.RecordMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IRecordManager;
import itacad.aliaksandrkryvapust.productmicroservice.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Component
public class RecordManager implements IRecordManager {
    private final static String recordPost = "New record was created";
    private final RecordMapper recordMapper;
    private final IRecordService recordService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final AuditMapper auditMapper;
    private final AuditManager auditManager;

    @Autowired
    public RecordManager(RecordMapper recordMapper, IRecordService recordService, JwtTokenUtil jwtTokenUtil,
                         UserService userService, AuditMapper auditMapper, AuditManager auditManager) {
        this.recordMapper = recordMapper;
        this.recordService = recordService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.auditMapper = auditMapper;
        this.auditManager = auditManager;
    }

    @Override
    public RecordDtoOutput save(RecordDtoInput dtoInput, HttpServletRequest request) {
        if (dtoInput.getRecipe()==null && dtoInput.getProduct()==null){
            throw new DataIntegrityViolationException("At least Meal or Recipe should not be null");
        }
        try {
            User user = getUser(request);
            Record record = this.recordService.save(recordMapper.inputMapping(dtoInput, user));
            AuditDto auditDto = this.auditMapper.recordOutputMapping(record, user, recordPost);
            this.auditManager.audit(auditDto);
            return recordMapper.outputMapping(record);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
        String username = jwtTokenUtil.getUsername(token);
        return this.userService.getUser(username);
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        return recordMapper.outputPageMapping(this.recordService.get(pageable));
    }

    @Override
    public RecordDtoOutput get(UUID id) {
        return recordMapper.outputMapping(this.recordService.get(id));
    }

    @Override
    public List<RecordDto> getRecordByTimeGap(ParamsDto paramsDto) {
        List<Record> records = this.recordService.getRecordByTimeGap(paramsDto);
        return this.recordMapper.listOutputMapping(records);
    }
}
