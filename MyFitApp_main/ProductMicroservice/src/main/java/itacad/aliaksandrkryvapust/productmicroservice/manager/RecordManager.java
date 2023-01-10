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
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IRecordManager;
import itacad.aliaksandrkryvapust.productmicroservice.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuditMapper auditMapper;
    private final AuditManager auditManager;

    @Autowired
    public RecordManager(RecordMapper recordMapper, IRecordService recordService,
                         AuditMapper auditMapper, AuditManager auditManager) {
        this.recordMapper = recordMapper;
        this.recordService = recordService;
        this.auditMapper = auditMapper;
        this.auditManager = auditManager;
    }

    @Override
    public RecordDtoOutput save(RecordDtoInput dtoInput, HttpServletRequest request) {
        if (dtoInput.getRecipe()==null && dtoInput.getProduct()==null){
            throw new DataIntegrityViolationException("At least Meal or Recipe should not be null");
        }
        try {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Record record = this.recordService.save(recordMapper.inputMapping(dtoInput, userDetails.getId()));
            AuditDto auditDto = this.auditMapper.recordOutputMapping(record, userDetails, recordPost);
            this.auditManager.audit(auditDto);
            return recordMapper.outputMapping(record);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return recordMapper.outputPageMapping(this.recordService.get(pageable, userDetails.getId()));
    }

    @Override
    public RecordDtoOutput get(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return recordMapper.outputMapping(this.recordService.get(id, userDetails.getId()));
    }

    @Override
    public List<RecordDto> getRecordByTimeGap(ParamsDto paramsDto) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Record> records = this.recordService.getRecordByTimeGap(paramsDto, userDetails.getId());
        return this.recordMapper.listOutputMapping(records);
    }
}
