package itacad.aliaksandrkryvapust.auditmicroservice.service;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.api.IAuditRepository;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.api.IUserRepository;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.auditmicroservice.service.api.IAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuditService implements IAuditService {
    private final IAuditRepository auditRepository;
    private final IUserRepository userRepository;

    @Autowired
    public AuditService(IAuditRepository auditRepository, IUserRepository userRepository) {
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Audit save(Audit audit) {
        Optional<User> currentUser = this.userRepository.findById(audit.getUser().getId());
        if (currentUser.isPresent()){
            if (!currentUser.get().equals(audit.getUser())){
                User user = this.userRepository.saveAndFlush(audit.getUser());
                audit.setUser(user);
            } else {
                audit.setUser(currentUser.get());
            }
        }
        return this.auditRepository.save(audit);
    }

    @Override
    public Page<Audit> get(Pageable pageable) {
        return this.auditRepository.findAll(pageable);
    }

    @Override
    public Audit get(UUID id) {
        return this.auditRepository.findById(id).orElseThrow();
    }
}
