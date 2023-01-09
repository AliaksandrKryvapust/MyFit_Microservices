package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IProfileRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return this.profileRepository.save(profile);
    }

    @Override
    public Page<Profile> get(Pageable pageable) {
        return this.profileRepository.findAll(pageable);
    }

    @Override
    public Profile get(UUID id) {
        return this.profileRepository.findById(id).orElseThrow();
    }
}
