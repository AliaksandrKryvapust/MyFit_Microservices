package itacad.aliaksandrkryvapust.auditmicroservice.service.transactional;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.api.IAuditRepository;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.api.IUserRepository;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.auditmicroservice.service.transactional.api.IAuditTransactionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuditTransactionalService implements IAuditTransactionalService {
    private final IAuditRepository auditRepository;
    private final IUserRepository userRepository;

    @Override
    public Audit saveTransactional(Audit audit) {
        setUserFromDatabase(audit);
        return auditRepository.save(audit);
    }

    private void setUserFromDatabase(Audit audit) {
        Optional<User> currentUser = userRepository.findById(audit.getUser().getId());
        if (currentUser.isPresent()){
            if (!currentUser.get().equals(audit.getUser()) && audit.getUser().getDtCreate()!=null){
                User user = userRepository.saveAndFlush(audit.getUser());
                audit.setUser(user);
            } else {
                audit.setUser(currentUser.get());
            }
        }
    }
}
