package itacad.aliaksandrkryvapust.productmicroservice.manager.api;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface IManagerDelete {
    void delete(UUID id, HttpServletRequest request);
}