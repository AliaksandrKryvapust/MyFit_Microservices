package itacad.aliaksandrkryvapust.productmicroservice.manager.api;


import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;

public interface IProductManager extends IManager<ProductDtoOutput, ProductDtoInput>,
        IManagerUpdate<ProductDtoOutput, ProductDtoInput>, IManagerDelete {
}
