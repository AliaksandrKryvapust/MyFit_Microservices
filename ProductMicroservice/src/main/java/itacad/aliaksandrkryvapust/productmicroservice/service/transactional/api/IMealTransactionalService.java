package itacad.aliaksandrkryvapust.productmicroservice.service.transactional.api;

import java.util.UUID;

public interface IMealTransactionalService {
    void deleteTransactional(UUID id, UUID userId);
}
