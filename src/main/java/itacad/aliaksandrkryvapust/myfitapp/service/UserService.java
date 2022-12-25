package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.repository.api.IUserRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User save(User user) {
        validate(user);
        return this.userRepository.save(user);
    }

    private void validate(User user) {
        if (user.getId() != null) {
            throw new IllegalStateException("User id should be empty");
        }
    }

    @Override
    public Page<User> get(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    public User get(UUID id) {
        return this.userRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public User update(User user, UUID id, Long version) {
        validate(user);
        User currentEntity = this.userRepository.findById(id).orElseThrow();
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("user table update failed, version does not match update denied");
        }
        updateEntityFields(user, currentEntity);
        return this.userRepository.save(currentEntity);
    }

    private void updateEntityFields(User user, User currentEntity) {
        currentEntity.setUsername(user.getUsername());
        currentEntity.setPassword(user.getPassword());
        currentEntity.setEmail(user.getEmail());
    }

    @Override
    public User getUser(String email) {
        return this.userRepository.findByEmail(email);
    }
}
