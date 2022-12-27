package itacad.aliaksandrkryvapust.myfitapp.service.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;

import java.util.List;
import java.util.UUID;

public interface IProductService extends IService<Product>, IServiceUpdate<Product>, IServiceDelete {
    List<Product> getByIds(List<UUID> uuids);
}
