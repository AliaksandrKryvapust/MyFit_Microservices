package itacad.aliaksandrkryvapust.myfitapp.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.myfitapp.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.RecordMapper;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IRecordManager;
import itacad.aliaksandrkryvapust.myfitapp.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Record;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.service.UserService;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
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
}
