package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProfileDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProfileDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.ProfileMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IProfileRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProfileManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProfileService;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IProfileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService, IProfileManager {
    private final static String PROFILE_POST = "New profile was created";
    private final IProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final IProfileValidator profileValidator;
    private final AuditMapper auditMapper;
    private final IAuditManager auditManager;

    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public Page<Profile> get(Pageable pageable, UUID userId) {
        return profileRepository.findAllByUser_UserId(pageable, userId);
    }

    @Override
    public Profile get(UUID id, UUID userId) {
        return profileRepository.findByIdAndUser_UserId(id, userId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public ProfileDtoOutput saveDto(ProfileDtoInput dtoInput) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile entityToSave = profileMapper.inputMapping(dtoInput, userDetails);
        profileValidator.validateEntity(entityToSave);
        Profile profile = save(entityToSave);
        prepareAudit(profile, userDetails, PROFILE_POST);
        return profileMapper.outputMapping(profile);
    }

    @Override
    public PageDtoOutput<ProfileDtoOutput> getDto(Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Profile> page = get(pageable, userDetails.getId());
        return profileMapper.outputPageMapping(page);
    }

    @Override
    public ProfileDtoOutput getDto(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile profile = get(id, userDetails.getId());
        return this.profileMapper.outputMapping(profile);
    }

    private void prepareAudit(Profile profile, MyUserDetails userDetails, String method) {
        AuditDto auditDto = auditMapper.profileOutputMapping(profile, userDetails, method);
        auditManager.audit(auditDto);
    }
}
