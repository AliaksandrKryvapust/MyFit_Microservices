package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IProfileRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {
    private final IProfileRepository profileRepository;

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

}
