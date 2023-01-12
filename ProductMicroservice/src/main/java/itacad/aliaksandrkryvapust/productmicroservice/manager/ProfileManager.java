package itacad.aliaksandrkryvapust.productmicroservice.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProfileDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProfileDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.ProfileMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IProfileManager;
import itacad.aliaksandrkryvapust.productmicroservice.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProfileService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.UUID;

@Component
public class ProfileManager implements IProfileManager {
    private final static String profilePost = "New profile was created";
    private final IProfileService profileService;
    private final ProfileMapper profileMapper;
    private final AuditManager auditManager;
    private final AuditMapper auditMapper;

    public ProfileManager(IProfileService profileService, ProfileMapper profileMapper, AuditManager auditManager,
                          AuditMapper auditMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
        this.auditManager = auditManager;
        this.auditMapper = auditMapper;
    }

    @Override
    public ProfileDtoOutput save(ProfileDtoInput dtoInput) {
        try {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Profile profile = this.profileService.save(profileMapper.inputMapping(dtoInput, userDetails));
            this.prepareAuditToSend(userDetails, profile);
            return this.profileMapper.outputMapping(profile);
        } catch (
                URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (
                JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.profileMapper.outputPageMapping(profileService.get(pageable, userDetails.getId()));
    }

    @Override
    public ProfileDtoOutput get(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.profileMapper.outputMapping(profileService.get(id, userDetails.getId()));
    }

    private void prepareAuditToSend(MyUserDetails userDetails, Profile profile) throws JsonProcessingException, URISyntaxException {
        AuditDto auditDto = this.auditMapper.profileOutputMapping(profile, userDetails, profilePost);
        this.auditManager.audit(auditDto);
    }
}
