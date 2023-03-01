package itacad.aliaksandrkryvapust.productmicroservice.service.api;


import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IManagerDelete;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IManagerUpdate;

public interface IProductManager extends IManager<ProductDtoOutput, ProductDtoInput>,
        IManagerUpdate<ProductDtoOutput, ProductDtoInput>, IManagerDelete {
}
