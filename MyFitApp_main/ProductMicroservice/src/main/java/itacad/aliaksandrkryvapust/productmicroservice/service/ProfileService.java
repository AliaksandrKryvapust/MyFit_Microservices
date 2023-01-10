package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IProfileRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProfileService implements IProfileService {
    private final IProfileRepository profileRepository;

    public ProfileService(IProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public Profile save(Profile profile) {
        this.validate(profile);
        return this.profileRepository.save(profile);
    }

    @Override
    public Page<Profile> get(Pageable pageable) {
        return this.profileRepository.findAll(pageable);
    }

    @Override
    public Profile get(UUID id, UUID userId) {
        Profile profile = this.profileRepository.findById(id).orElseThrow();
        this.checkCredentials(userId, profile);
        return profile;
    }

    private void validate(Profile profile) {
        if (profile.getId()!=null && profile.getDtUpdate()!=null){
            throw new IllegalStateException("Profile id & version should be empty");
        }
    }

    private void checkCredentials(UUID userId, Profile profile) {
        if (!profile.getUser().getUser_id().equals(userId)){
            throw new BadCredentialsException("It`s forbidden to modify not private data");
        }
    }
}
